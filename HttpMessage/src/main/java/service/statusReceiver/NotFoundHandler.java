package service.statusReceiver;

import http.core.HttpResponse;
import http.util.HttpStatus;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class NotFoundHandler implements BaseHandler {
    /**
     * 状态码: 404
     * - 请求失败，请求所希望得到的资源未被在服务器上发现。
     * 没有信息能够告诉用户这个状况到底是暂时的还是永久的。
     * 假如服务器知道情况的话，应当使用410状态码来告知旧资源因为某些内部的配置机制问题，
     * 已经永久的不可用，而且没有任何可以跳转的地址。
     * <p>
     * 404这个状态码被广泛应用于当服务器不想揭示到底为何请求被拒绝或者没有其他适合的响应可用的情况下。
     * -
     */
    @Override
    public Map<String, Object> handleStatus(HttpResponse response) {
        Map<String, Object> ans = new HashMap<>();
        ans.put("status", HttpStatus.CODE_404);
        //响应头
        ans.put("Response Headers", response.getHeader().getHeaderText(Charset.forName("utf-8")));
        return ans;
    }
}
