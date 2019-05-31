package http.util.header;

/**
 * 实体首部类
 *
 * @author steve
 */
interface BodyHeader {
    // 实体信息性首部
    String ALLOW = "Allow";
    String LOCATION = "Location";

    // 内容首部
    String CONTENT_BASE = "Content-Base";
    String CONTENT_ENCODING = "Content-Encoding";
    String CONTENT_LANGUAGE = "Content-Language";
    String CONTENT_LENGTH = "Content-Length";
    String CONTENT_LOCATION = "Content-Location";
    String CONTENT_MD5 = "Content-MD5";
    String CONTENT_RANGE = "Content-Range";
    String CONTENT_TYPE = "Content-Type";

    // 缓存头部
    String ETAG = "ETag";
    String EXPIRES = "Expires";
    String LAST_MODIFIED = "Last-Modified";
}
