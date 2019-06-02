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
     *
     * @param response     :http 响应
     * @param baseLocation :缓存基路径
     * @return :缓存的绝对地址
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
        //map to file .json
        file = new File(path + new Date().toString() + ".json");
        if (!file.exists()) {
            boolean res = file.createNewFile();
        }
        String content = JSONObject.toJSONString(response);
        bw = new BufferedWriter(new FileWriter(file));
        bw.write(content);
        bw.flush();
        return file.getPath();
    }

    /**
     * 清空缓存内容
     */
    public static void cleanUpCache(String baseLocation) {

    }


    //字节流转为字符串
    public static String streamToString(InputStream inputStream, String charset) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charset));
            String s;
            StringBuilder builder = new StringBuilder();
            while ((s = bufferedReader.readLine()) != null) {
                builder.append(s);
            }

            bufferedReader.close();
            return builder.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
