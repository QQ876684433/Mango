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
        File directory = new File(".");
        String path = null;
        path = directory.getCanonicalPath();//获取当前路径
        path += baseLocation;
        File file = new File(path);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdir();
        }
        file = new File(path + new Date().toString()+".json");
        if (!file.exists()) {
            boolean res = file.createNewFile();
        }
        String content = JSONObject.toJSONString(response);
        bw = new BufferedWriter(new FileWriter(file));
        bw.write(content);
        bw.flush();
        return file.getPath();
    }

    //TODO: 实现不同文件 content-Type 的解析
}
