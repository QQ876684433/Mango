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

###### 1.4.1.1 http包

封装与解析 http 的请求与响应内容，提供接口给其他所有模块之间的调用



###### 1.4.1.2 service包



具体的解析内容涉及 `数据逻辑` 部分的内容。具体内容参见第三部分

### 2、HttpRequest类：解析和封装HTTP请求报文

#### 2.1 请求行：

- method：POST（上传文件到服务端的URL路径下）、GET（获取服务端的URL路径下的文件或者文件夹内容列表）
- url：使用相对路径
- version：HTTP/1.1（默认长连接，即Connection: keep-alive）

#### 2.2 请求头（Header）：

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

#### 2.3 请求主体（requestBody）:@Nullable

#### 2.4 方法接口

##### 2.4.1 包装请求报文

- setMethod(HTTP_METHOD_POST或者HTTP_METHOD_POST)
- setUrl(String url)：url为全路径或者相对路径都可，例如localhost:80/getXXX[?id=xxx&name=xxx]，参数部分可为中文，内部会转成ascii表示
- setHttpVersion(HTTP_VERSION_1_1或者HTTP_VERSION_1_0)，不设置默认为HTTP_VERSION_1_1
- getHeader().setCacheControl(CACHE_CONTROL_NO_CACHE/...)
- getHeader().setConnection(CONNECTION_LONG或者CONNECTION_SHORT)，不设置则根据http version来设置默认值
- getHeader().setContentType(String contentType)
- setRequestBody(InputStream is)

##### 2.4.2 若干getter方法

##### 2.4.3 解析请求报文：

HttpRequest parse(InputStream is)

##### 2.4.4 传输请求报文

void writeTo(OutputStream os)

##### 2.4.5 获取http报文文本格式：

toString()

#### 1.5 文件响应处理模块

应对如下几种数据类型 (`Content-Type`) :

1. text/plain
2. image/png（.png文件）
3.  audio/mp3（.mp3文件）
4.  video/mpeg4（.mp4文件）

分别进行数据流的解析。其中，考虑到 `JavaFX` 本身 `API` 的限制，对于 `text / plain ` 和 `image / png` 格式进行展示，而 `audio / mp3` 与 `video / mpeg4` 直接进行字节流的显示

### 2. 数据逻辑

具体文档如下

------



|  版本   |   内容   |  修改时间  | 修改者 |
| :-----: | :------: | :--------: | :----: |
| v 0.0.1 | 起草文档 | 2019/05/18 |  chph  |



### 1、HttpResponse类：解析和封装HTTP响应报文

#### 1.1 响应状态行：

##### 1.1.1 version：根据请求报文来定

##### 1.1.2 statusCode：

- 200：请求已成功，请求所希望的响应头或数据体将随此响应返回。
- 301：被请求的资源已永久移动到新位置，并且将来任何对此资源的引用都应该使用本响应返回的若干个 URI 之一。
- 302：请求的资源现在临时从不同的 URI 响应请求。由于这样的重定向是临时的，客户端应当继续向原有地址发送以后的请求。**只有在Cache-Control或Expires中进行了指定的情况下，这个响应才是可缓存的**。
- 304：如果客户端发送了一个带条件的 GET 请求且该请求已被允许，而文档的内容（自上次访问以来或者根据请求的条件）并没有改变，则服务器应当返回这个状态码。304响应禁止包含消息体，因此始终以消息头后的第一个空行结尾。
- 404：请求失败，请求所希望得到的资源未被在服务器上发现。405：请求行中指定的请求方法不能被用于请求相应的资源。该响应必须返回一个Allow 头信息用以表示出当前资源能够接受的请求方法的列表。
- 500：服务器遇到了一个未曾预料的状况

##### 1.1.3 message

#### 1.2 响应头（Header）：

- allow：对某网络资源的有效的请求行为，不允许则返回405
- *<u>cacheControl：告诉所有的缓存机制是否可以缓存及哪种类型</u>*
- contentLength：响应体的长度
- contentLocation：请求资源可替代的备用的另一地址
- contentType：响应体的内容类型
- date：服务器发出响应的时间
- <u>*eTag：请求变量的实体标签的当前值*</u>
- <u>*expires：响应过期的日期和时间*</u>
- location：用来重定向接收方到非请求URL的位置来完成请求或标识新的资源（返回3xx的状态码时，可以通过将该字段设为请求url重新请求资源）

#### 1.3 响应主体（ResponseBody）：@Nullable

#### 1.4 方法接口

##### 1.4.1 包装响应报文

- setVersion(HTTP_VERSION_1_1或者HTTP_VERSION_1_0)
- setStatus(STATUS_CODE_200/...)		// message则内部根据code自动设置
- getHeader().setAllow(HTTP_METHOD_GET/HTTP_METHOD_POST)
- getHeader().setCacheControl(CACHE_CONTROL_NO_CACHE/...)
- getHeader().setContentLocation(可替代请求资源的相对url)
- getHeader().setContentType(String contentType)
- getHeader().setETag(String eTag)
- getHeader().setExpires(String yyyy-MM-dd_hh:mm:ss)
- getHeader().setLocation(重定向资源的相对url)

##### 1.4.2 若干getter方法

##### 1.4.3 解析响应报文

HttpResponse parse(InputStream is)

##### 1.4.4 传输响应报文

void writeTo(OutputStream os)

##### 1.4.5 获取http报文文本格式：

toString()

### 3. 流程说明

###### 写完代码再加

### 4. 实现结果

###### 写完代码再加