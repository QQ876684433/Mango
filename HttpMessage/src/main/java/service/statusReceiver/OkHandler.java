package service.statusReceiver;

import http.core.HttpResponse;

import java.util.Map;

public class OkHandler implements BaseHandler {
    /**
     * 状态码: 200
     * 具体请求方法返回的内容
     * GET:响应中发送对应于所请求资源的实体;
     * <p>
     * POST:一个描述或包含动作结果的实体;
     *
     * @return
     */
    @Override
    public Map<String, Object> handleStatus(HttpResponse response) {
        //TODO: GET,POST 请求分析数据类型(content-Type),缓存本地,给出路径展示;
        return null;
    }
}
