package service;

import http.core.HttpRequest;
import http.core.HttpResponse;

public interface HttpRequestHandler {
    public HttpResponse processRequest(HttpRequest httpRequest);
}
