package service;

import http.core.HttpRequest;
import http.core.HttpResponse;
import http.util.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务注册器类
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/6/5
 */
public class ServiceRegister {
    static final Map<String, ServerService> serviceChain = new HashMap<>();

    static {
        //注册服务
        serviceChain.put("/file", FileService.getInstance());
    }


    public static HttpResponse handleRequest(HttpRequest request) {
        HttpResponse response = null;
        for (Map.Entry<String, ServerService> e : serviceChain.entrySet()) {
            if (request.getUrl().startsWith(e.getKey())) {
                response = e.getValue().processRequest(request);
                break;
            }
        }
        if (response == null) {
            response = new HttpResponse();
            response.setStatus(HttpStatus.CODE_404);
        }
        return response;
    }

}
