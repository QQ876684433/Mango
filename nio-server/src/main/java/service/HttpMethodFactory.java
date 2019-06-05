package service;

import http.core.HttpRequest;

public class HttpMethodFactory {
    public static HttpRequestHandler getHttpMethod(HttpRequest httpRequest){
        // TODO 返回请求方法的简单工厂
        switch (httpRequest.getMethod()){
            case http.util.HttpMethod.GET:
                return new GetMethodHandler();
            case http.util.HttpMethod.POST:
                return new PostMethodHandler();
        }
        return null;
    }
}
