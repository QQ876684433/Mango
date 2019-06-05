package service;

import http.core.HttpRequest;
import http.core.HttpResponse;

public class HttpContext {
    private HttpRequestHandler httpMethod;

    public HttpContext(HttpRequestHandler httpMethod) {
        this.httpMethod = httpMethod;
    }

    public HttpResponse processRequest(HttpRequest httpRequest) {
        return httpMethod.processRequest(httpRequest);
    }
}
