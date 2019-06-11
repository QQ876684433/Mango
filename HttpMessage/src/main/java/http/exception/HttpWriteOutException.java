package http.exception;

import java.io.IOException;

/**
 * Http报文写出到输出流失败异常
 *
 * @author steve
 */
public class HttpWriteOutException extends IOException {
    public HttpWriteOutException(String msg){
        super(msg);
    }
}
