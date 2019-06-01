package http.util.handler;

import http.core.HttpResponse;

/**
 * 对HTTP响应报文字段处理的高层封装工具类
 *
 * @author steve
 */
public class HttpResponseUtils extends HttpUtils {
    private static final String CONNECTION_KEEP_ALIVE = "keep-alive";
    private static final String CONNECTION_CLOSE = "close";

    /**
     * HttpResponse实例
     */
    private HttpResponse httpResponse;

    public HttpResponseUtils(HttpResponse httpResponse) {
        super(httpResponse.getHeader());
        this.httpResponse = httpResponse;
    }

}
