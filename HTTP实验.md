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

#### 2.1、HttpResponse类：解析和封装HTTP响应报文

##### 2.1.1 响应状态行：

###### 2.1.1.1 version：根据请求报文来定

###### 2.1.1.2 statusCode：

- 200：请求已成功，请求所希望的响应头或数据体将随此响应返回。
- 301：被请求的资源已永久移动到新位置，并且将来任何对此资源的引用都应该使用本响应返回的若干个 URI 之一。如果可能，拥有链接编辑功能的客户端应当自动把请求的地址修改为从服务器反馈回来的地址。除非额外指定，否则这个响应也是可缓存的。　　新的永久性的 URI 应当在响应的 Location 域中返回。除非这是一个 HEAD 请求，否则响应的实体中应当包含指向新的 URI 的超链接及简短说明。　　如果这不是一个 GET 或者 HEAD 请求，因此浏览器禁止自动进行重定向，除非得到用户的确认，因为请求的条件可能因此发生变化。　　注意：对于某些使用 HTTP/1.0 协议的浏览器，当它们发送的 POST 请求得到了一个301响应的话，接下来的重定向请求将会变成 GET 方式。
- 302：请求的资源现在临时从不同的 URI 响应请求。由于这样的重定向是临时的，客户端应当继续向原有地址发送以后的请求。**只有在Cache-Control或Expires中进行了指定的情况下，这个响应才是可缓存的**。　　新的临时性的 URI 应当在响应的 Location 域中返回。除非这是一个 HEAD 请求，否则响应的实体中应当包含指向新的 URI 的超链接及简短说明。　　如果这不是一个 GET 或者 HEAD 请求，那么浏览器禁止自动进行重定向，除非得到用户的确认，因为请求的条件可能因此发生变化。　　注意：虽然RFC 1945和RFC 2068规范不允许客户端在重定向时改变请求的方法，但是很多现存的浏览器将302响应视作为303响应，并且使用 GET 方式访问在 Location 中规定的 URI，而无视原先请求的方法。状态码303和307被添加了进来，用以明确服务器期待客户端进行何种反应。
- 304：如果客户端发送了一个带条件的 GET 请求且该请求已被允许，而文档的内容（自上次访问以来或者根据请求的条件）并没有改变，则服务器应当返回这个状态码。304响应禁止包含消息体，因此始终以消息头后的第一个空行结尾。　　该响应必须包含以下的头信息：　　**Date**，除非这个服务器没有时钟。假如没有时钟的服务器也遵守这些规则，那么代理服务器以及客户端可以自行将 Date 字段添加到接收到的响应头中去（正如RFC 2068中规定的一样），缓存机制将会正常工作。　　**ETag 和/或 Content-Location**，假如同样的请求本应返回200响应。　　**Expires, Cache-Control，和/或Vary**，假如其值可能与之前相同变量的其他响应对应的值不同的话。　　假如本响应请求使用了强缓存验证，那么本次响应不应该包含其他实体头；否则（例如，某个带条件的 GET 请求使用了弱缓存验证），本次响应禁止包含其他实体头；这避免了缓存了的实体内容和更新了的实体头信息之间的不一致。　　假如某个304响应指明了当前某个实体没有缓存，那么缓存系统必须忽视这个响应，并且重复发送不包含限制条件的请求。　　假如接收到一个要求更新某个缓存条目的304响应，那么缓存系统必须更新整个条目以反映所有在响应中被更新的字段的值。
- 404：请求失败，请求所希望得到的资源未被在服务器上发现。没有信息能够告诉用户这个状况到底是暂时的还是永久的。假如服务器知道情况的话，应当使用410状态码来告知旧资源因为某些内部的配置机制问题，已经永久的不可用，而且没有任何可以跳转的地址。404这个状态码被广泛应用于当服务器不想揭示到底为何请求被拒绝或者没有其他适合的响应可用的情况下。
- 405：请求行中指定的请求方法不能被用于请求相应的资源。该响应必须返回一个Allow 头信息用以表示出当前资源能够接受的请求方法的列表。　　鉴于 PUT，DELETE 方法会对服务器上的资源进行写操作，因而绝大部分的网页服务器都不支持或者在默认配置下不允许上述请求方法，对于此类请求均会返回405错误。
- 500：服务器遇到了一个未曾预料的状况，导致了它无法完成对请求的处理。一般来说，这个问题都会在服务器的程序码出错时出现。

###### 2.1.1.3 message

##### 2.1.2 响应头（Header）：

- allow：对某网络资源的有效的请求行为，不允许则返回405
- *<u>cacheControl：告诉所有的缓存机制是否可以缓存及哪种类型</u>*
- contentLength：响应体的长度
- contentLocation：请求资源可替代的备用的另一地址
- contentType：响应体的内容类型
- date：服务器发出响应的时间
- <u>*eTag：请求变量的实体标签的当前值*</u>
- <u>*expires：响应过期的日期和时间*</u>
- location：用来重定向接收方到非请求URL的位置来完成请求或标识新的资源（返回3xx的状态码时，可以通过将该字段设为请求url重新请求资源）

##### 2.1.3 响应主体（ResponseBody）：@Nullable



#### 2.2、HttpRequest类：解析和封装HTTP请求报文

##### 2.2.1 请求行：

- method：POST（上传文件到服务端的URL路径下）、GET（获取服务端的URL路径下的文件或者文件夹内容列表）
- url：使用相对路径
- version：HTTP/1.1（默认长连接，即Connection: keep-alive）

##### 2.2.2 请求头（Header）：

- accept（接受的内容类型）：text/plain; charset=utf-8（文本文件）、image/png（.png文件）、audio/mp3（.mp3文件）、video/mpeg4（.mp4文件）
- Cache-Control：指定请求和响应遵循的缓存机制
- connection（是否为长连接，keep-alive为长连接，close为短连接或者关闭之前的长连接）
- ContentType：text/plain; charset=utf-8（文本文件）、image/png（.png文件）、audio/mp3（.mp3文件）、video/mpeg4（.mp4文件）
  - type （/左边为主类型）
  - subtype（/右边为子类型）
  - parameter（;右边是参数，这里只有charset这个参数）
- contentLength：请求的内容长度
- date：发送请求的日期和时间
- host：服务端主机名，本地则为localhost

##### 2.2.3 请求主体（requestBody）:@Nullable




### 3. 流程说明

#### 3.1 资源说明与测试用例说明

所有的资源文件存储于 `noi-server/src/main/resources`目录下，url请求基路径 `localhost:8080/file`



### 4. 实现结果

- 图片文件(`img`)传输

![body.png](https://github.com/CaesarRoot/Mango/blob/master/body.png?raw=true)



![demo avatar.png](https://github.com/CaesarRoot/Mango/blob/master/demo%20avatar.png?raw=true)

- 普通文本文件传输 `text/plain`





- 音频文件传输





- 视频文件传输