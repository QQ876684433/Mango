package http.core;

import http.exception.HttpHeaderParseFailException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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
     * @param is 首部输入流
     */
    Header(InputStream is) throws IOException {
        this();

        int buffer;
        byte[] bf;
        try {
            bf = new byte[is.available()];
            int pointer = 0;
            // 记录是否读取到首部结束的标志位，即'\n'连续出现两次说明首部已经读取完成
            boolean isEndOfLine = false;
            while (true) {
                buffer = is.read();
                if (buffer == '\n') {
                    if (isEndOfLine) break;
                    isEndOfLine = true;

                    String line = new String(bf, Charset.defaultCharset());
                    int splitIndex = line.indexOf(":");

                    // 当读取到无效Header项(即不包含':'符号)时，则退出首部解析
                    if (splitIndex == -1) break;
                    this.setProperty(line.substring(0, splitIndex).trim(), line.substring(splitIndex + 1).trim());

                    bf = new byte[is.available()];
                    pointer = 0;
                } else {
                    isEndOfLine = false;
                    bf[pointer++] = (byte) buffer;
                }
            }
        } catch (IOException e) {
            throw new HttpHeaderParseFailException("解析首部出错！");
        }
    }

    /**
     * 获取所有已经设置的首部
     *
     * @return 所有首部的键值对
     */
    public Properties getHeaders() {
        Properties props = new Properties();
        props.putAll(headers);
        return props;
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
     * @param charset 首部文本的编码
     * @return 首部文本
     */
    public String getHeaderText(Charset charset) {
        ByteArrayOutputStream bos = (ByteArrayOutputStream) this.getHeaderOutputStream(charset);
        return bos.toString();
    }

    /**
     * 获取首部文本
     *
     * @return 首部文本
     */
    public String getHeaderText() {
        return getHeaderText(Charset.defaultCharset());
    }

    /**
     * 获取首部输出流
     *
     * @param charset 编码
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

    /**
     * 获取首部输出流
     *
     * @return 首部输出流
     */
    private OutputStream getHeaderOutputStream() {
        return getHeaderOutputStream(Charset.defaultCharset());
    }
}
