package http.core;

/**
 * HTTP响应报文类
 *
 * @author steve
 */
public class HttpResponse {
    private int version;
    private int status;
    private String message = null;
    private Header header = null;
    private ResponseBody responseBody = null;
}
