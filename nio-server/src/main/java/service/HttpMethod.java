package service;

import http.core.HttpRequest;
import http.core.HttpResponse;

public interface HttpMethod {
    public HttpResponse processRequest(HttpRequest httpRequest);
}
