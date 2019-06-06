package service;

import http.core.HttpRequest;
import http.core.HttpResponse;

public class PostMethodHandler implements HttpRequestHandler {
    @Override
    public HttpResponse processRequest(HttpRequest httpRequest) {
        //暂时弃用
        return null;
    }
}
