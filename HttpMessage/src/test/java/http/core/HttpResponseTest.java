package http.core;

import http.util.header.ResponseHeader;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class HttpResponseTest {
    private HttpResponse httpResponse;

    @Before
    @Test
    public void setUp() throws Exception {
        String response = "HTTP/1.1 200 OK\n" +
                "Date: Sat, 31 Dec 2005 23:59:59 GMT\n" +
                "Content-Type: text/html;charset=ISO-8859-1\n" +
                "Content-Length: 122\n" +
                "\n" +
                "＜html＞\n" +
                "＜head＞\n" +
                "＜title＞Wrox Homepage＜/title＞\n" +
                "＜/head＞\n" +
                "＜body＞\n" +
                "＜!-- body goes here --＞\n" +
                "＜/body＞\n" +
                "＜/html＞\n";
        InputStream is = new ByteArrayInputStream(response.getBytes());
        httpResponse = new HttpResponse(is);
        Assert.assertEquals("HTTP/1.1", httpResponse.getVersion());
        Assert.assertEquals(200, httpResponse.getStatus());
        Assert.assertEquals("OK", httpResponse.getMessage());
        Assert.assertEquals("Sat, 31 Dec 2005 23:59:59 GMT", httpResponse.getHeader().getProperty(ResponseHeader.DATE));
        Assert.assertEquals("text/html;charset=ISO-8859-1", httpResponse.getHeader().getProperty(ResponseHeader.CONTENT_TYPE));
        Assert.assertEquals("122", httpResponse.getHeader().getProperty(ResponseHeader.CONTENT_LENGTH));
        Assert.assertEquals("＜html＞\n" +
                "＜head＞\n" +
                "＜title＞Wrox Homepage＜/title＞\n" +
                "＜/head＞\n" +
                "＜body＞\n" +
                "＜!-- body goes here --＞\n" +
                "＜/body＞\n" +
                "＜/html＞\n", httpResponse.getResponseBodyText());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void setStatus() {
    }

    @Test
    public void addHeader() {
    }

    @Test
    public void getResponseBodyText() {
    }

    @Test
    public void getResponseBodyStream() {
    }

    @Test
    public void setResponseBody() {
    }

    @Test
    public void setResponseBody1() {
    }

    @Test
    public void writeTo() {
    }

    @Test
    public void toString1() {
        Assert.assertEquals("HTTP/1.1 200 OK\n" +
                "Content-Length:122\n" +
                "Date:Sat, 31 Dec 2005 23:59:59 GMT\n" +
                "Content-Type:text/html;charset=ISO-8859-1\n" +
                "\n" +
                "\n" +
                "＜html＞\n" +
                "＜head＞\n" +
                "＜title＞Wrox Homepage＜/title＞\n" +
                "＜/head＞\n" +
                "＜body＞\n" +
                "＜!-- body goes here --＞\n" +
                "＜/body＞\n" +
                "＜/html＞\n", httpResponse.toString());
    }

    @Test
    public void setVersion() {
    }

    @Test
    public void getVersion() {
    }

    @Test
    public void getStatus() {
    }

    @Test
    public void getMessage() {
    }

    @Test
    public void getHeader() {
    }
}