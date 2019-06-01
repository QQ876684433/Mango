package service.statusReceiver;

import http.core.HttpResponse;

import java.util.Map;

public class NotFoundHandler implements BaseHandler {
    @Override
    public Map<String, Object> handleStatus(HttpResponse response) {
        return null;
    }
}
