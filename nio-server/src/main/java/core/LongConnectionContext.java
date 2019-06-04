package core;

import lombok.Getter;

import java.util.Date;

/**
 * 保存一个http长连接相关信息的类
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/6/4
 */

public class LongConnectionContext {
    @Getter
    //所建立的连接已发送过的请求数
    private int requestsServed;

    @Getter
    //tcp连接建立时间, 标准: UTC
    private long initTime;

    public LongConnectionContext() {
        requestsServed = 0;
        initTime = new Date().getTime();
    }

    public void addRequestsServedCount() {
        requestsServed++;
    }
}
