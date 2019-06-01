package service;

import http.core.HttpResponse;
import org.junit.Assert;
import org.junit.Test;
import service.statusReceiver.BaseHandler;
import service.statusReceiver.MovPermHandler;
import service.statusReceiver.StatusHandler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

public class StatusTest {

    @Test
    public void movPermHandlerTest() {
        String response = "HTTP/1.1 301  MovedPermanently\n" +
                "Date: Sat, 31 Dec 2005 23:59:59 GMT\n" +
                "Content-Type: text/html;charset=ISO-8859-1\n" +
                "Content-Length: 122\n" +
                "\n" +
                "uri to current resource";
        HttpResponse httpResponse = mockResponse(response);
        Assert.assertNotNull(httpResponse);
        Map map = StatusHandler.handle(httpResponse);
        Assert.assertEquals("uri to current resource\n", map.get("href"));
    }

    private HttpResponse mockResponse(String text) {
        InputStream is = new ByteArrayInputStream(text.getBytes());
        return new HttpResponse(is);
    }
}
