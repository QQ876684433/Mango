package http.util.header;

/**
 * 通用首部类
 *
 * @author steve
 */
interface CommonHeader {
    // 通用的信息性首部
    String CONNECTION = "Connection";
    String DATE = "Date";
    String MINE_VERSION = "MINE-Version";
    String TRAILER = "Trailer";
    String TRANSFER_ENCODING = "Transfer-Encoding";
    String UPDATE = "Update";
    String VIA = "Via";

    // 通用缓存性首部
    String CACHE_CONTROLL = "Cache-Control";
    String PRAGMA = "Pragma";
}
