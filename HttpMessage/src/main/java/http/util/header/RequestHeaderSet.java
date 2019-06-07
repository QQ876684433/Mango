package http.util.header;

import lombok.Getter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 暂时使用的记录所有请求头的集合
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/6/4
 */
public class RequestHeaderSet {
    @Getter
    private static final Set<String> headerSet = new HashSet<>();

    static {
        headerSet.addAll(Arrays.asList(
                "Allow",
                "Location",
                "Content-Base",
                "Content-Encoding",
                "Content-Language",
                "Content-Length",
                "Content-Location",
                "Content-MD5",
                "Content-Range",
                "Content-Type",
                "ETag",
                "Expires",
                "Last-Modified",

                "Connection",
                "Keep-Alive",
                "Date",
                "MINE-Version",
                "Trailer",
                "Transfer-Encoding",
                "Update",
                "Via",
                "Cache-Control",
                "Pragma",

                "Client-IP",
                "From",
                "Host",
                "Referer",
                "UA-Color",
                "UA-CPU",
                "UA-Disp",
                "UA-OS",
                "UA-Pixels",
                "User-Agent",
                "Accept",
                "Accept-Charset",
                "Accept-Encoding",
                "Accept-Language",
                "TE",
                "Expect",
                "If-Match",
                "If-Modified-Since",
                "If-None-Match",
                "If-Range",
                "If-Unmodified-Since",
                "Range",
                "Authorization",
                "Cookie",
                "Cookie2",
                "Max-Forward",
                "Proxy-Authorization",
                "Proxy-Connection"
        ));
    }
}
