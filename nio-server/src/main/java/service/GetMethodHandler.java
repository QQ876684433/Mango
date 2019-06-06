package service;

import http.core.HttpRequest;
import http.core.HttpResponse;

public class GetMethodHandler implements HttpRequestHandler {
    @Override
    public HttpResponse processRequest(HttpRequest httpRequest) {
        String url = httpRequest.getUrl();
        HttpResponse httpResponse = new HttpResponse();
//        if (url.startsWith("/file"))
//            getFile(httpRequest, httpResponse);

        return httpResponse;
    }


}
