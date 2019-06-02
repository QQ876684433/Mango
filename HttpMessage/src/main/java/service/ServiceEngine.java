
package service;

import http.core.HttpRequest;
import http.core.HttpResponse;
import http.util.HttpMethod;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 接受返回http报文
 * 处理最终报文状态的响应内容
 */
public class ServiceEngine {
    private HttpRequest httpRequest;
    private HttpResponse httpResponse;

    public Map<String, String> doHttpRequest() {
        //TODO: Finish the encapsulation
        return null;
    }

    private void doHttpRequest(Map<String, String> params) {
        httpRequest = new HttpRequest();
        httpRequest.setMethod(HttpMethod.GET);
        //设置headers
        Set<String> headerKeys = headerMap.keySet();
        headerKeys.forEach(key -> {
            httpRequest.setHeader(key, headerMap.get(key));
        });
    }

}

