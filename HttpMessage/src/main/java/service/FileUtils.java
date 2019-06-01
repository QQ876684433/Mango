package service;

import com.alibaba.fastjson.JSONObject;
import http.core.HttpResponse;

import java.io.*;
import java.util.Date;
import java.util.Map;

/**
 * 响应缓存/文件存储
 */
public class FileUtils {
    private static BufferedWriter bw;
    private static BufferedReader br;

    /**
     * 缓存http响应内容
     */
    public static String cacheResponse(HttpResponse response, String baseLocation) throws IOException {
        File file = new File(baseLocation + new Date().toString());
//        if (!file.exists()) {
//            file.createNewFile();
//        }
//        String content = JSONObject.toJSONString(response);
//        bw = new BufferedWriter(new FileWriter(file));
//        bw.write(content);
//        bw.flush();
        return file.getPath();
    }

}
