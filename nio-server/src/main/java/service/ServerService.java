package service;

import http.core.HttpRequest;
import http.core.HttpResponse;

/**
 * 服务器提供的服务接口
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/6/5
 */
public interface ServerService {
    public HttpResponse processRequest(HttpRequest httpRequest);
}
