package service;

import http.core.HttpRequest;
import http.core.HttpResponse;
import http.util.HttpStatus;
import http.util.header.RequestHeader;
import http.util.header.ResponseHeader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class GetMethodHandler implements HttpRequestHandler {
    @Override
    public HttpResponse processRequest(HttpRequest httpRequest) {
        String url = httpRequest.getUrl();
        HttpResponse httpResponse = new HttpResponse();
        try {
            InputStream inputStream = new FileInputStream(new File(url));
            httpResponse.setResponseBody(inputStream);
            httpResponse.addHeader(
                    ResponseHeader.CONTENT_TYPE,
                    httpRequest.getHeader().getProperty(RequestHeader.CONTENT_TYPE));
            httpResponse.setStatus(HttpStatus.CODE_200);
            return new HttpResponse(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("文件不存在");
            httpResponse.setStatus(HttpStatus.CODE_404);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpResponse;
    }
}
