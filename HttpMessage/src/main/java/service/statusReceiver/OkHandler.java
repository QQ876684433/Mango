package service.statusReceiver;

import http.core.HttpResponse;
import http.util.HttpStatus;
import http.util.header.ResponseHeader;
import service.FileUtils;
import service.fileTrans.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class OkHandler implements BaseHandler {
    private FileTransformer fileTransformer;

    /**
     * 状态码: 200
     * 具体请求方法返回的内容
     * GET:响应中发送对应于所请求资源的实体;
     * <p>
     * POST:一个描述或包含动作结果的实体;
     *
     * 默认进行cache缓存
     * @return
     */
    @Override
    public Map<String, Object> handleStatus(HttpResponse response) throws IOException {
        //TODO: GET,POST 请求分析数据类型(content-Type),缓存本地,给出路径展示;
        switch (response.getHeader().getProperty("Content-Type")) {
            case "text/plain":
                this.fileTransformer = new TextTransformer();
                break;
            case "image/png":
                this.fileTransformer = new ImageTransformer();
                break;
            case "audio/mp3":
                this.fileTransformer = new AudioTransformer();
                break;
            case "video/mpeg4":
                this.fileTransformer = new VideoTransformer();
                break;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("status", HttpStatus.CODE_200);
        map.put("file", this.fileTransformer.transform(response.getResponseBodyStream()));
        if (null == response.getHeader().getProperty(ResponseHeader.CACHE_CONTROLL) ||
                !response.getHeader()
                        .getProperty(ResponseHeader.CACHE_CONTROLL).equals("no-cache")) { //已指定不需要设置缓存
            map.put("cache-location", FileUtils.cacheResponse(response, "/cache/"));
        }
        return map;
    }
}
