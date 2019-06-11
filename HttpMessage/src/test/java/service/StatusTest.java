package service;

import http.core.HttpResponse;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.Map;

public class StatusTest {

    @Test
    public void movPermHandlerTest() throws IOException {
        String response = "HTTP/1.1 301  MovedPermanently\n" +
                "Date: Sat, 31 Dec 2005 23:59:59 GMT\n" +
                "Content-Type: text/html;charset=ISO-8859-1\n" +
                "Content-Length: 122\n" +
                "\n" +
                "uri to current resource";
        HttpResponse httpResponse = mockResponse(response);
        InputStream in = httpResponse.getResponseBodyStream();
        String s = streamToString(in, "utf-8");
        Assert.assertNotNull(httpResponse);
        Map map = StatusHandler.handle(httpResponse);
        Assert.assertEquals("uri to current resource\n", map.get("href"));
    }

    @Test
    public void NotFoundTest() {
        String response = "HTTP/1.1 404  NotFound\n" +
                "Date: Sat, 31 Dec 2005 23:59:59 GMT\n" +
                "Content-Type: text/html;charset=ISO-8859-1\n" +
                "Content-Length: 122\n" +
                "\n";
        HttpResponse httpResponse = mockResponse(response);
        Assert.assertNotNull(httpResponse);
        Map ans = null;
        ans = StatusHandler.handle(httpResponse);
        Assert.assertEquals(404, ans.get("status"));
    }

    @Test
    public void MethodNotAllowedTest() {
        String response = "HTTP/1.1 405  MethodNotAllowed\n" +
                "Date: Sat, 31 Dec 2005 23:59:59 GMT\n" +
                "Allow: POST , PUT\n" +
                "Content-Type: text/html;charset=ISO-8859-1\n" +
                "Content-Length: 122\n" +
                "\n";
        HttpResponse httpResponse = mockResponse(response);
        Assert.assertNotNull(httpResponse);
        Map ans = null;
        ans = StatusHandler.handle(httpResponse);
        Assert.assertEquals(405, ans.get("status"));
        Assert.assertEquals("POST , PUT", ans.get("Allow")); //指定allow的方法类型
    }

    private HttpResponse mockResponse(String text) {
        try {
            InputStream is = new ByteArrayInputStream(text.getBytes());
            return new HttpResponse(is);
        } catch (Exception e) {
            return null;
        }
    }

    //字节流转为字符串的方法
    private String streamToString(InputStream inputStream, String charset) {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charset);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String s = null;
            StringBuilder builder = new StringBuilder();
            while ((s = bufferedReader.readLine()) != null) {
                builder.append(s);
            }

            bufferedReader.close();
            return builder.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
