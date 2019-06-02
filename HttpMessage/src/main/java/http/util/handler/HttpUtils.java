package http.util.handler;

import http.core.Header;
import http.util.header.ResponseHeader;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HTTP报文抽象工具类
 */
abstract public class HttpUtils {
    private static final String CONNECTION_KEEP_ALIVE = "keep-alive";
    private static final String CONNECTION_CLOSE = "close";
    private static final String KEEP_ALIVE_TIMEOUT = "timeout";
    private static final String KEEP_ALIVE_MAX = "max";

    /**
     * HTTP请求报文或者HTTP响应报文首部实例
     */
    private Header header;

    HttpUtils(Header header) {
        this.header = header;
    }

    /**
     * 判断是否是长连接
     *
     * @return keep-alive: true, close: false
     */
    public boolean isLongConnection() {
        String conType = header.getProperty(ResponseHeader.CONTENT_TYPE);
        return CONNECTION_KEEP_ALIVE.equalsIgnoreCase(conType);
    }

    /**
     * 获取Keep-Alive首部的属性
     *
     * @return Properties对象，包括KEEP_ALIVE_TIMEOUT和KEEP_ALIVE_MAX
     */
    public Properties getLongConnectionDuration() {
        String pattern = "timeout=(\\d+)[,]?[\\s]?[max=]*(\\d*)";
        String keepAlive = header.getProperty(ResponseHeader.KEEP_ALIVE);
        Matcher matcher = Pattern.compile(pattern).matcher(keepAlive);
        Properties conProps = new Properties();
        while (matcher.find()) {
            conProps.put(KEEP_ALIVE_TIMEOUT, matcher.group(1));
            conProps.put(KEEP_ALIVE_MAX, matcher.group(2));
        }
        return conProps;
    }
}
