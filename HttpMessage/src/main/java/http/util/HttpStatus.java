package http.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Http状态码和对应的message
 *
 * @author steve
 */
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
        MESSAGE.put(CODE_500, "Internal Server Error");
    }
}
