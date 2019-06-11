package http.util.handler;

import http.core.HttpResponse;
import org.junit.Test;

public class HttpUtilsTest {

    @Test
    public void getMessageDate() {
        HttpResponse response = new HttpResponse();
        HttpResponseUtils responseUtils = new HttpResponseUtils(response);
        System.out.println(DateUtils.dateToStrDay(responseUtils.getMessageDate()));
    }
}