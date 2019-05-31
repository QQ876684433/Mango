package http.core;

import java.io.InputStream;
import java.io.OutputStream;
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
        this.headers = new HashMap<String, String>();
    }

    /**
     * 解析报文首部输入流构建Header实例
     * @param headersStream 首部输入流
     */
    Header(InputStream headersStream) {
        this();
        // todo：解析输入流，构造Header实例
    }

    /**
     * 设置报文首部属性
     *
     * @see http.util.header.RequestHeader
     * @see http.util.header.ResponseHeader
     * @param key 首部键
     * @param value key对应的值
     * @return 返回Header实例自身，支持链式调用
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
    public String getProperty(String key){
        return headers.get(key);
    }

    /**
     * 获取首部文本
     * @return 首部文本
     */
    public String getHeaderText(){
        // todo 生成首部文本
        return null;
    }

    /**
     * 获取首部输出流
     * @return 首部输出流
     */
    public OutputStream getHeaderOutputStream(){
        // todo 生成首部的输出流
        return null;
    }
}
