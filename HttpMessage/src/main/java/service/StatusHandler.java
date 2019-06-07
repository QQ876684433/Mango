package service;

import http.core.HttpResponse;
import service.statusReceiver.*;

import java.io.IOException;
import java.util.Map;

/**
 * 处理不同的响应码
 * 在接收服务端响应码后执行特定行为
 */
public class StatusHandler {
    //响应码内容
    private static BaseHandler handler;

    /**
     * 提供其他端调用的接口
     * StatusHandler.handle(HttpResponse response)
     */
    public static Map<String, ?> handle(HttpResponse response) throws IOException {
        switch (response.getStatus()) {
            case 200:
                handler = new OkHandler();
                break;
            case 301:
                handler = new MovPermHandler();
                break;
            case 302:
                handler = new FoundHandler();
                break;
            case 304:
                handler = new NotModifiedHandler();
                break;
            case 404:
                handler = new NotFoundHandler();
                break;
            case 405:
                handler = new MethodNotAllowedHandler();
                break;
            case 500:
                handler = new InternalErrorHandler();
                break;
            default:
                handler = new UnrecognizedStatusHandler();
        }
        return handler.handleStatus(response);
    }
}
