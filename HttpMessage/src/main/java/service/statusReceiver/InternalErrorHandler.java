package service.statusReceiver;

import http.core.HttpResponse;
import http.util.HttpStatus;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class InternalErrorHandler implements BaseHandler {
    /**
     * 状态码：500
     * 服务器遇到了一个未曾预料的状况，导致了它无法完成对请求的处理。一般来说，这个问题都会在服务器的程序码出错时出现。
     */
    @Override
    public Map<String, Object> handleStatus(HttpResponse response) {
        Map<String, Object> ans = new HashMap<>();
        ans.put("status", HttpStatus.CODE_500);
        //响应头
        ans.put("Response Headers", response.getHeader().getHeaderText(Charset.forName("utf-8")));
        return ans;
    }
}
