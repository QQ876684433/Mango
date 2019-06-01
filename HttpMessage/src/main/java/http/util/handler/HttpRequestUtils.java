package http.util.handler;

import http.core.HttpRequest;

/**
 * 对HTTP请求报文字段处理的高层封装工具类
 *
 * @author steve
 */
public class HttpRequestUtils extends HttpUtils {

    /**
     * HttpRequest的实例
     */
    private HttpRequest httpRequest;

    public HttpRequestUtils(HttpRequest httpRequest) {
        super(httpRequest.getHeader());
        this.httpRequest = httpRequest;
    }

}
