package http.core;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP报文首部类
 *
 * @author steve
 */
public class Header {
    private Map<String, String> headers;

    Header() {
        this.headers = new HashMap<>();
    }

    /**
     * 解析报文首部输入流构建Header实例
     *
     * @param br 首部输入流
     */
    Header(BufferedReader br) {
        this();
        try {
            String line;
            while (!(line = br.readLine()).isEmpty()) {
                int splitIndex = line.indexOf(":");
                this.setProperty(line.substring(0, splitIndex).trim(), line.substring(splitIndex + 1).trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置报文首部属性
     *
     * @param key   首部键
     * @param value key对应的值
     * @return 返回Header实例自身，支持链式调用
     * @see http.util.header.RequestHeader
     * @see http.util.header.ResponseHeader
     */
    Header setProperty(String key, String value) {
        headers.put(key, value);
        return this;
    }

    /**
     * 获取请求头的值
     *
     * @param key 请求头某项的key
     * @return 请求头某项的key对应的value
     */
    public String getProperty(String key) {
        return headers.get(key);
    }

    /**
     * 获取首部文本
     *
     * @param charset
     * @return 首部文本
     */
    public String getHeaderText(Charset charset) {
        ByteArrayOutputStream bos = (ByteArrayOutputStream) this.getHeaderOutputStream(charset);
        return bos.toString();
    }

    /**
     * 获取首部输出流
     *
     * @return 首部输出流
     */
    private OutputStream getHeaderOutputStream(Charset charset) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            for (String key : headers.keySet()) {
                os.write(key.getBytes(charset));
                os.write(":".getBytes(charset));
                os.write(headers.get(key).getBytes(charset));
                os.write("\n".getBytes(charset));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return os;
    }
}
