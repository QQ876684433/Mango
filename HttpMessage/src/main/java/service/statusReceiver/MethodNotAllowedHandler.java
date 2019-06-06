package service.statusReceiver;

import http.core.HttpResponse;
import http.util.HttpStatus;
import http.util.header.ResponseHeader;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class MethodNotAllowedHandler implements BaseHandler {
    /**
     * 状态码：405
     * 方法不可用
     * 请求行中指定的请求方法不能被用于请求相应的资源。
     * 该响应必须返回一个Allow 头信息用以表示出当前资源能够接受的请求方法的列表。
     * 鉴于 PUT，DELETE 方法会对服务器上的资源进行写操作，
     * 因而绝大部分的网页服务器都不支持或者在默认配置下不允许上述请求方法，对于此类请求均会返回405错误。
     */
    @Override
    public Map<String, Object> handleStatus(HttpResponse response) {
        Map<String, Object> ans = new HashMap<>();
        ans.put("status", HttpStatus.CODE_405);
        ans.put("Allow", response.getHeader().getProperty(ResponseHeader.ALLOW));
        //响应头
        ans.put("Response Headers", response.getHeader().getHeaderText(Charset.forName("utf-8")));
        return ans;
    }
}
