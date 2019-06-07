## 基于Raw Socket实现HTTP实验

### 1. 功能描述

#### 1.1 模块简介

利用NIO线程管理方式，基于运输层 `socket` 实现 `http` 协议。具体模块可分为 `NIO模块` 、`GUI模块`、`http中间解析模块`、`文件响应处理模块`

#### 1.2 NIO实现服务器端线程维护

- 用于服务端的请求、响应处理。常启一个**主线程**，当有新的http请求发送至服务器端时，开辟新的线程，将该请求加入一个连接池队列中，由 **主线程** 定时对线程池进行轮训。

- 对于查询到的请求，如果是长连接请求，那么直接进行响应处理，返回给 GUI 部分
- 如果是短连接请求，基于短连接请求是否失效来判断下一步的操作
  - 如果短连接未失效，则进行请求的处理，请求出线程池队列
  - 如果短连接失效，则将该对应的子线程杀死

#### 1.3 GUI模块

模仿 `PostMan`的设计，其图形页面显示如下：

![UI0.png](https://github.com/CaesarRoot/Mango/blob/master/UI0.png?raw=true)



其中给予

**输入框** ：填写发送请求的 `URL`

**请求发送类型下拉单**：选择发送请求的具体类型。可支持 `GET，POST`

如上图，为 `GET` 方法。直接在 `Params` 中进行编辑，自动进行 `http` 请求的封装

___

同时支持对 http 请求的请求头进行编辑，可以加入标准的请求头字段，来进行更加具体的http 请求操作。例如 `Cache-Control` 来进行响应的缓存 

![UI1.png](https://github.com/CaesarRoot/Mango/blob/master/UI1.png?raw=true)

_____

此外，系统还支持对于 `POST` 方法的请求体封装，支持如下几种形式

- form-data
- x-www-form-urlencoded
- raw
- binary

![UI2.png](https://github.com/CaesarRoot/Mango/blob/master/UI2.png?raw=true)



#### 1.4 http中间解析模块

##### 1.4.1 http-message模块的组成

![mango-httpmessage.png](https://github.com/CaesarRoot/Mango/blob/master/HttpMessage/src/main/resources/dependencies/mango-httpmessage.png?raw=true)

$$
总体模块依赖关系图
$$


![mango-httpmessage-http.png](https://github.com/CaesarRoot/Mango/blob/master/HttpMessage/src/main/resources/dependencies/mango-httpmessage-http.png?raw=true)
$$
http解析模块依赖关系图
$$


###### 1.4.1.1 http包

封装与解析 http 的请求与响应内容，提供接口给其他所有模块之间的调用

![](https://i.loli.net/2019/06/07/5cfa1e905d30159613.png)

主要有三个包：

（1）core：请求报文、响应报文和首部的抽象类

![](https://i.loli.net/2019/06/07/5cfa1f6d74e5f93553.png)

- HttpResponse和HttpRequest类核心方法

  数据结构如下：

  ```java
  public class HttpRequest {
      private String method;
      private String url;
      private String version;
  	private Header header;
  	private HttpBody requestBody = null;
  
      public HttpRequest() {
          header = new Header();
      }
  
      public HttpRequest(InputStream requestInputStream) throws Exception {
          this();
          this.parse(requestInputStream);
      }
      
      public void writeTo(OutputStream outputStream){
      	...
      }
  }
  
  ```

  ```java
  public class HttpResponse {
      private String method;
      private String url;
      private String version;
  	private Header header;
  	private HttpBody responseBody = null;
  
      public HttpResponse() {
          header = new Header();
      }
  
      public HttpResponse(InputStream responseInputStream) throws Exception {
          this();
          this.parse(responseInputStream);
      }
      
      public void writeTo(OutputStream outputStream){
      	...
      }
  }
  ```

  使用socket输入流作为构造函数的参数，即可构建出HttpResponse和HttpRequest的实例，即将报文解析成Java对象；当客户端或者服务端发送报文时，只需要将socket输出流传入writeTo方法中即可；体现了数据职责和行为职责的统一

- Header类的核心代码

  数据结构如下：

  ```java
  public class Header {
      private Map<String, String> headers;
  
      Header() {
          this.headers = new HashMap<>();
          this.headers.put(CommonHeader.DATE, DateUtils.dateToStrDay(new Date()));
      }
  
      /**
       * 解析报文首部输入流构建Header实例
       *
       * @param is 首部输入流
       */
      Header(InputStream is) throws IOException {
  		...
      }
  
      /**
       * 获取所有已经设置的首部
       *
       * @return 所有首部的键值对
       */
      public Properties getHeaders() {
          Properties props = new Properties();
          props.putAll(headers);
          return props;
      }
  
      Header setProperty(String key, String value) {
          headers.put(key, value);
          return this;
      }
  
      public String getProperty(String key) {
          return headers.get(key);
      }
  
      public String getHeaderText() {
          return getHeaderText(Charset.defaultCharset());
      }
  
      private OutputStream getHeaderOutputStream() {
  		...
      }
  }
  ```

  将首部字段以键值对的形式存放在HashMap中，在解析报文时，可以将输入流传入Header的构造函数中即可完成首部字段的解析并返回Header对象；在发送报文时，可以直接调用`getHeaderOutputStream()`将首部字段以流的形式输出

- HttpBody类核心代码

  数据结果如下：

  ```java
  public class HttpBody {
      /**
       * 媒体类型
       */
      private MediaType mediaType;
  
      /**
       * 实体部分
       */
      private byte[] content;
  	
  	HttpBody(String contentType, InputStream content) throws Exception {
  		...
      }
  
      public InputStream getContent() {
          return new ByteArrayInputStream(this.content);
      }
  
      void setContent(InputStream is) {
  		...
      }
  }
  
  ```

  实体部分会有一个对应的MediaType，即媒体类型，以便再获取输出流时可以以正确的编码输出

  

（2）util：提供io和报文字符串的更细粒度的解析工具，以及常见的报文字段常量定义

![](https://i.loli.net/2019/06/07/5cfa20565516a91430.png)

- 起始行字段常量：HttpMethod、HttpVersion和HttpStatus

  ```java
  // 请求方式
  public class HttpMethod {
      public static final String POST = "POST";
      public static final String GET = "GET";
  }
  
  // Http状态码和对应的message
  public final class HttpStatus {
      public static final int CODE_200 = 200;
      public static final int CODE_301 = 301;
      public static final int CODE_302 = 302;
      public static final int CODE_304 = 304;
      public static final int CODE_404 = 404;
      public static final int CODE_405 = 405;
      public static final int CODE_500 = 500;
  
      public static final Map<Integer, String> MESSAGE = new HashMap<Integer, String>();
  
      // 初始化状态码对应的消息
      static {
          MESSAGE.put(CODE_200, "OK");
          MESSAGE.put(CODE_301, "Move Permanently");
          MESSAGE.put(CODE_302, "Found");
          MESSAGE.put(CODE_304, "Not Modified");
          MESSAGE.put(CODE_404, "Not Found");
          MESSAGE.put(CODE_405, "Method Not Allowed");
          MESSAGE.put(CODE_500, "Internal Server Error");
      }
  }
  
  // HTTP协议版本
  public final class HttpVersion {
      public static final String HTTP_VERSION_1_0 = "HTTP/1.0";
      public static final String HTTP_VERSION_1_1 = "HTTP/1.1";
  }
  ```

  由于这些字段在报文中是固定而且统一的，定义成常量供其他模块使用可以保证一致性，同时HttpStatus中的MESSAGE可以使得响应报文在设置状态码时可以自动将状态码消息也自动设置上去

- HTTP协议报文有四中类型的首部，分别是通用首部（CommonHeader）、实体首部（BodyHeader）、请求首部（RequestHeader）和响应首部（ResponseHeader）

  ![](https://i.loli.net/2019/06/07/5cfa27fd6235b65952.png)

  可以通过RequestHeader和ResponseHeader继承CommonHeader和BodyHeader中的首部字段，然后分别提供给HttpRequest和HttpResponse使用，也可以保证首部字段的一致性，并减少硬编码易出错问题

（3）exception：报文解析的异常处理类

![](https://i.loli.net/2019/06/07/5cfa209b5c35b72328.png)

具体的解析内容涉及 `数据逻辑` 部分的内容。具体内容参见第二部分

###### 1.4.1.2 service包

- 文件类型解析

  实现对于 text/plainimage/png（.png文件）audio/mp3（.mp3文件）video/mpeg4（.mp4文件）类型文件的解析工作，在 `OkHandler` 中进行调用；如果服务端成功返回了 `200` 的响应码，并且为 `GET` 方法，那么就表明成功进行了一次资源获取。

- 响应码解析

  对于不同的响应码，给出不同的行为

具体的解析内容涉及 `数据逻辑` 部分的内容。具体内容参见第二部分



#### 1.5 文件响应处理模块

应对如下几种数据类型 (`Content-Type`) :

1. text/plain
2. image/png（.png文件）
3.  audio/mp3（.mp3文件）
4.  video/mpeg4（.mp4文件）

分别进行数据流的解析。其中，考虑到 `JavaFX` 本身 `API` 的限制，对于 `text / plain ` 和 `image / png` 格式进行展示，而 `audio / mp3` 与 `video / mpeg4` 直接进行字节流的显示

### 2. 数据逻辑






### 3. 流程说明

#### 3.1 资源说明与测试用例说明

所有的资源文件存储于 `noi-server/src/main/resources`目录下，url请求基路径 `localhost:8080/file`



### 4. 实现结果

