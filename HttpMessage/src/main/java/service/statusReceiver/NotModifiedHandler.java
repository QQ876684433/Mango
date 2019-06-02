package service.statusReceiver;

import http.core.HttpResponse;
import http.util.HttpStatus;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NotModifiedHandler implements BaseHandler {
    /**
     * 状态码：304
     * 未修改
     * 该响应必须包含以下的头信息：
     *
     * @Date: 除非这个服务器没有时钟。假如没有时钟的服务器也遵守这些规则，
     * 那么代理服务器以及客户端可以自行将 Date 字段添加到接收到的响应头中去（正如RFC 2068中规定的一样），缓存机制将会正常工作
     * @ Content-Location，假如同样的请求本应返回200响应。
     * @ Cache-Control ，假如其值可能与之前相同变量的,其他响应对应的值不同的话。
     */
    @Override
    public Map<String, Object> handleStatus(HttpResponse response) {
        Map<String, Object> ans = new HashMap<>();
        ans.put("status", HttpStatus.CODE_304);

        String headerContent = response.getHeader().getHeaderText(Charset.forName("utf-8"))
                .concat("Date :" + new Date().toString() + "\n") //TODO: content location
                .concat("Content-Location :" + "content location" + "\n");
        //响应头
        ans.put("Response Headers", headerContent);
        return ans;
    }
}
