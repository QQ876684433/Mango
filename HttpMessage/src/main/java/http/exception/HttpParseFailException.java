package http.exception;

import java.io.IOException;

/**
 * 报文解析失败异常
 *
 * @author steve
 */
public class HttpParseFailException extends IOException {
    public HttpParseFailException(String s) {
        super(s);
    }
}
