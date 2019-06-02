package service.statusReceiver;

import http.core.HttpResponse;
import http.util.HttpStatus;
import service.FileUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.InvalidPathException;
import java.util.HashMap;
import java.util.Map;

public class FoundHandler implements BaseHandler {
    /**
     * 状态码：302
     * 临时移动 , 客户端仍使用原有的uri
     * 请求的资源现在临时从不同的 URI 响应请求。
     * 由于这样的重定向是临时的，客户端应当继续向原有地址发送以后的请求
     */
    @Override
    public Map<String, Object> handleStatus(HttpResponse response) {
        try {
            Map<String, Object> ans = new HashMap<>();
            ans.put("status", HttpStatus.CODE_302);
            if (null == response.getHeader().getProperty("Cache-Control") ||
                    !response.getHeader()
                            .getProperty("Cache-Control").equals("no-cache")) { //已指定不需要设置缓存
                ans.put("cache-location", FileUtils.cacheResponse(response, "/cache/"));
            }
            //响应头
            ans.put("Response Headers", response.getHeader().getHeaderText(Charset.forName("utf-8")));
            //重定向路径
            ans.put("href", response.getResponseBodyText());

            return ans;
        } catch (IOException e) {
            e.printStackTrace();
            throw new InvalidPathException("", "File already exist");
        }
    }
}
