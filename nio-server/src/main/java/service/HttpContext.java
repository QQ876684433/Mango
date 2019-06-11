package service;

import http.core.HttpRequest;
import http.core.HttpResponse;

public class HttpContext {

    public HttpResponse processRequest(HttpRequest httpRequest) {
        //TODO 展示新的处理逻辑
//        return httpMethod.processRequest(httpRequest);
        return ServiceRegister.handleRequest(httpRequest);
    }
}
