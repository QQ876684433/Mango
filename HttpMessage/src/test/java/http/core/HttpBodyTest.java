package http.core;

import http.util.header.ResponseHeader;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

public class HttpBodyTest {

    @Test
    public void setContent() {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.addHeader(ResponseHeader.CONTENT_TYPE, "image/png");
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("imgs/avatar.jpg");
            System.out.println(is.available());
            httpResponse.setResponseBody(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(httpResponse.getResponseBodyText());
    }
}