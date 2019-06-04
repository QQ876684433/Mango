package util;

import lombok.Getter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 暂时使用的记录所有响应头的集合
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/6/4
 */
public class ResponseHeaderSet {
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

                "Age",
                "Public",
                "Retry-After",
                "Server",
                "Title",
                "Warning",
                "Accept-Ranges",
                "Vary",
                "Proxy-Authenticate",
                "WWW-Authenticate",
                "Set-Cookie",
                "Set-Cookie2"
        ));
    }


}
