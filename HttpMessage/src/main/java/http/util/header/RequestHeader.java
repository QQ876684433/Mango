package http.util.header;

/**
 * 请求首部类
 *
 * @author steve
 * @see http.util.header.CommonHeader
 * @see http.util.header.BodyHeader
 */
public class RequestHeader implements BodyHeader, CommonHeader {
    // 请求信息性首部
    public static final String CLIENT_IP = "Client-IP";
    public static final String FROM = "From";
    public static final String HOST = "Host";
    public static final String REFERER = "Referer";
    public static final String UA_COLOR = "UA-Color";
    public static final String UA_CPU = "UA-CPU";
    public static final String UA_DISP = "UA-Disp";
    public static final String UA_OS = "UA-OS";
    public static final String UA_PIXELS = "UA-Pixels";
    public static final String USER_AGENT = "User-Agent";

    // Accept首部
    public static final String ACCEPT = "Accept";
    public static final String ACCEPT_CHARSET = "Accept-Charset";
    public static final String ACCEPT_ENCODING = "Accept-Encoding";
    public static final String ACCEPT_LANGUAGE = "Accept-Language";
    public static final String TE = "TE";

    // 条件请求首部
    public static final String EXPECT = "Expect";
    public static final String IF_MATCH = "If-Match";
    public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
    public static final String IF_NONE_MATCH = "If-None-Match";
    public static final String IF_RANGE = "If-Range";
    public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
    public static final String RANGE = "Range";

    // 安全请求首部
    public static final String AUTHORIZATION = "Authorization";
    public static final String COOKIE = "Cookie";
    public static final String COOKIE2 = "Cookie2";

    // 代理请求首部
    public static final String MAX_FORWARD = "Max-Forward";
    public static final String PROXY_AUTHORIZATION = "Proxy-Authorization";
    public static final String PROXY_CONNECTION = "Proxy-Connection";
}
