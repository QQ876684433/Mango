package http.exception;

/**
 * 报文首部解析失败异常
 *
 * @author steve
 */
public class HttpHeaderParseFailException extends HttpParseFailException {
    public HttpHeaderParseFailException(String s) {
        super(s);
    }
}
