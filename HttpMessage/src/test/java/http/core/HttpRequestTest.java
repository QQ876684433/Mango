package http.core;

import http.util.HttpMethod;
import http.util.header.RequestHeader;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class HttpRequestTest {
    private HttpRequest httpRequest;

    @Before
    @Test
    public void setUp() throws Exception {
        String request = "POST /search?hl=zh-CN&source=hp&q=domety&aq=f&oq= HTTP/1.1  \n" +
                "Accept: image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/vnd.ms-excel, application/vnd.ms-powerpoint, " +
                "application/msword, application/x-silverlight, application/x-shockwave-flash, */*  \n" +
                "Referer: <a href=\"http://www.google.cn/\">http://www.google.cn/</a>  \n" +
                "Accept-Language: zh-cn  \n" +
                "Accept-Encoding: gzip, deflate  \n" +
                "Content-Type: text/html; charset=utf-8  \n" +
                "User-Agent: Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; TheWorld)  \n" +
                "Host: <a href=\"http://www.google.cn\">www.google.cn</a>  \n" +
                "Connection: Keep-Alive  \n" +
                "Cookie: PREF=ID=80a06da87be9ae3c:U=f7167333e2c3b714:NW=1:TM=1261551909:LM=1261551917:S=ybYcq2wpfefs4V9g; " +
                "NID=31=ojj8d-IygaEtSxLgaJmqSjVhCspkviJrB6omjamNrSm8lZhKy_yMfO2M4QMRKcH1g0iQv9u-2hfBW7bUFwVh7pGaRUb0RnHcJU37y-" +
                "FxlRugatx63JLv7CWMD6UB_O_r  \n\nasjfaoijfdaoijfd\n";
        InputStream is = new ByteArrayInputStream(request.getBytes());
        httpRequest = new HttpRequest(is);
        Assert.assertEquals(HttpMethod.POST, httpRequest.getMethod());
        Assert.assertEquals("/search?hl=zh-CN&source=hp&q=domety&aq=f&oq=", httpRequest.getUrl());
        Assert.assertEquals("HTTP/1.1", httpRequest.getVersion());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void setHeader() {
        httpRequest.setHeader(RequestHeader.CONTENT_LENGTH, "123");
        Assert.assertEquals("123", httpRequest.getHeader().getProperty(RequestHeader.CONTENT_LENGTH));
    }

    @Test
    public void getRequestBodyText() {
        Assert.assertEquals("asjfaoijfdaoijfd\n", httpRequest.getRequestBodyText());
    }

    @Test
    public void getRequestBodyStream() {
        byte[] expect = "asjfaoijfdaoijfd\n".getBytes();
        byte[] actual = new byte[1];
        int buffer;
        InputStream is = httpRequest.getRequestBodyStream();
        try {
            byte[] temp = new byte[is.available()];
            int pointer = 0;
            while ((buffer = is.read()) != -1) {
                temp[pointer++] = (byte) buffer;
            }
            actual = Arrays.copyOf(temp, pointer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(new String(expect), new String(actual));
    }

    @Test
    public void setRequestBody() {
        try {
            httpRequest.setRequestBody("qwertyuiop\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertEquals("qwertyuiop\n", httpRequest.getRequestBodyText());
    }

    @Test
    public void setRequestBody1() {

    }

    @Test
    public void writeTo() {
    }

    @Test
    public void toString1() {
    }

    @Test
    public void setMethod() {
    }

    @Test
    public void setUrl() {
    }

    @Test
    public void setVersion() {
    }

    @Test
    public void getMethod() {
    }

    @Test
    public void getUrl() {
    }

    @Test
    public void getVersion() {
    }

    @Test
    public void getHeader() {
    }
}