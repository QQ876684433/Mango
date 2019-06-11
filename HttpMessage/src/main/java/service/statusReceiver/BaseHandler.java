package service.statusReceiver;

import http.core.HttpResponse;

import java.io.IOException;
import java.util.Map;

public interface BaseHandler {
    /**
     * 父接口
     *
     * @return*/
    Map<String, Object> handleStatus(HttpResponse response) ;
}
