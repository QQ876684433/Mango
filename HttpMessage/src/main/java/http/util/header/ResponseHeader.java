package http.util.header;

/**
 * 响应首部类
 *
 * @author steve
 * @see http.util.header.CommonHeader
 * @see http.util.header.BodyHeader
 */
public class ResponseHeader implements CommonHeader, BodyHeader {
    // 响应的信息性首部
    public static final String AGE = "Age";
    public static final String PUBLIC = "Public";
    public static final String RETRY_AFTER = "Retry-After";
    public static final String SERVER = "Server";
    public static final String TITLE = "Title";
    public static final String WARNING = "Warning";

    // 协商首部
    public static final String ACCEPT_RANGES = "Accept-Ranges";
    public static final String VARY = "Vary";

    // 安全响应首部
    public static final String PROXY_AUTHENTICATE = "Proxy-Authenticate";
    public static final String WWW_AUTHENTICATE = "WWW-Authenticate";
    public static final String SET_COOKIE = "Set-Cookie";
    public static final String SET_COOKIE2 = "Set-Cookie2";
}
