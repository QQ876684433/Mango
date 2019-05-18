## HTTP请求和响应报文接口定义



|  版本   |   内容   |  修改时间  | 修改者 |
| :-----: | :------: | :--------: | :----: |
| v 0.0.1 | 起草文档 | 2019/05/18 |  chph  |



### 1、HttpResponse类：解析和封装HTTP响应报文

#### 1.1 响应状态行：

##### 1.1.1 version：根据请求报文来定

##### 1.1.2 statusCode：

- 200：请求已成功，请求所希望的响应头或数据体将随此响应返回。
- 301：被请求的资源已永久移动到新位置，并且将来任何对此资源的引用都应该使用本响应返回的若干个 URI 之一。如果可能，拥有链接编辑功能的客户端应当自动把请求的地址修改为从服务器反馈回来的地址。除非额外指定，否则这个响应也是可缓存的。　　新的永久性的 URI 应当在响应的 Location 域中返回。除非这是一个 HEAD 请求，否则响应的实体中应当包含指向新的 URI 的超链接及简短说明。　　如果这不是一个 GET 或者 HEAD 请求，因此浏览器禁止自动进行重定向，除非得到用户的确认，因为请求的条件可能因此发生变化。　　注意：对于某些使用 HTTP/1.0 协议的浏览器，当它们发送的 POST 请求得到了一个301响应的话，接下来的重定向请求将会变成 GET 方式。
- 302：请求的资源现在临时从不同的 URI 响应请求。由于这样的重定向是临时的，客户端应当继续向原有地址发送以后的请求。**只有在Cache-Control或Expires中进行了指定的情况下，这个响应才是可缓存的**。　　新的临时性的 URI 应当在响应的 Location 域中返回。除非这是一个 HEAD 请求，否则响应的实体中应当包含指向新的 URI 的超链接及简短说明。　　如果这不是一个 GET 或者 HEAD 请求，那么浏览器禁止自动进行重定向，除非得到用户的确认，因为请求的条件可能因此发生变化。　　注意：虽然RFC 1945和RFC 2068规范不允许客户端在重定向时改变请求的方法，但是很多现存的浏览器将302响应视作为303响应，并且使用 GET 方式访问在 Location 中规定的 URI，而无视原先请求的方法。状态码303和307被添加了进来，用以明确服务器期待客户端进行何种反应。
- 304：如果客户端发送了一个带条件的 GET 请求且该请求已被允许，而文档的内容（自上次访问以来或者根据请求的条件）并没有改变，则服务器应当返回这个状态码。304响应禁止包含消息体，因此始终以消息头后的第一个空行结尾。　　该响应必须包含以下的头信息：　　**Date**，除非这个服务器没有时钟。假如没有时钟的服务器也遵守这些规则，那么代理服务器以及客户端可以自行将 Date 字段添加到接收到的响应头中去（正如RFC 2068中规定的一样），缓存机制将会正常工作。　　**ETag 和/或 Content-Location**，假如同样的请求本应返回200响应。　　**Expires, Cache-Control，和/或Vary**，假如其值可能与之前相同变量的其他响应对应的值不同的话。　　假如本响应请求使用了强缓存验证，那么本次响应不应该包含其他实体头；否则（例如，某个带条件的 GET 请求使用了弱缓存验证），本次响应禁止包含其他实体头；这避免了缓存了的实体内容和更新了的实体头信息之间的不一致。　　假如某个304响应指明了当前某个实体没有缓存，那么缓存系统必须忽视这个响应，并且重复发送不包含限制条件的请求。　　假如接收到一个要求更新某个缓存条目的304响应，那么缓存系统必须更新整个条目以反映所有在响应中被更新的字段的值。
- 404：请求失败，请求所希望得到的资源未被在服务器上发现。没有信息能够告诉用户这个状况到底是暂时的还是永久的。假如服务器知道情况的话，应当使用410状态码来告知旧资源因为某些内部的配置机制问题，已经永久的不可用，而且没有任何可以跳转的地址。404这个状态码被广泛应用于当服务器不想揭示到底为何请求被拒绝或者没有其他适合的响应可用的情况下。
- 405：请求行中指定的请求方法不能被用于请求相应的资源。该响应必须返回一个Allow 头信息用以表示出当前资源能够接受的请求方法的列表。　　鉴于 PUT，DELETE 方法会对服务器上的资源进行写操作，因而绝大部分的网页服务器都不支持或者在默认配置下不允许上述请求方法，对于此类请求均会返回405错误。
- 500：服务器遇到了一个未曾预料的状况，导致了它无法完成对请求的处理。一般来说，这个问题都会在服务器的程序码出错时出现。

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