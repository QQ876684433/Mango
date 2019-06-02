package service;

import http.core.HttpRequest;

public class Client {
    public static void main(String[] args){
        HttpRequest httpRequest = new HttpRequest();
        HttpContext httpContext = new HttpContext(HttpMethodFactory.getHttpMethod(httpRequest));
        httpContext.processRequest(httpRequest);
    }
}
