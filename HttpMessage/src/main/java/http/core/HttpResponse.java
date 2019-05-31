package http.core;

import com.sun.istack.internal.Nullable;
import http.util.HttpStatus;
import http.util.header.RequestHeader;
import http.util.header.ResponseHeader;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * HTTP响应报文类
 *
 * @author steve
 */
public class HttpResponse {
    /**
     * http协议版本
     *
     * @see http.util.HttpVersion
     */
    @Getter
    @Setter
    @NonNull
    private int version;

    /**
     * http响应状态码
     */
    @Getter
    @NonNull
    private int status;

    /**
     * http响应状态消息
     */
    @Getter
    @NonNull
    private String message = null;

    /**
     * http响应首部
     */
    @Getter
    @NonNull
    private Header header;

    /**
     * http响应实体
     */
    @Nullable
    private HttpBody responseBody = null;

    public HttpResponse() {
        this.header = new Header();
    }

    /**
     * 使用输入流构建HttpResponse对象
     * @param responseInputStream 响应报文输入流
     */
    public HttpResponse(InputStream responseInputStream){
        this();
        this.parse(responseInputStream);
    }


    //==============================================
    //              响应状态行设置
    //==============================================

    /**
     * 设置状态码，参考值
     * @see HttpStatus
     * @param status 状态码
     */
    public void setStatus(int status) {
        // 自动设置status code对应的message
        this.message = HttpStatus.MESSAGE.get(status);
        this.status = status;
    }


    //==============================================
    //              响应首部设置
    //==============================================

    /**
     * 支持链式调用
     * @param key 首部键
     * @param value 对应key的属性值
     * @return 返回HttpResponse实例本身，支持链式调用
     */
    public HttpResponse addHeader(String key, String value) {
        this.header.setProperty(key, value);
        return this;
    }


    //==============================================
    //              响应实体
    //==============================================

    /**
     * 获取实体部分文本内容
     *
     * @return 响应实体文本内容
     */
    public String getResponseBodyText() {
        if (this.responseBody == null) throw new NullPointerException("当前响应实体为空！");
        return responseBody.getTextContent();
    }

    /**
     * 获取实体部分输入流
     *
     * @return 响应实体输入流
     */
    public InputStream getResponseBodyStream() {
        if (this.responseBody == null) throw new NullPointerException("当前响应实体为空！");
        return responseBody.getContent();
    }

    /**
     * 设置响应实体
     *
     * @param responseBody 响应实体输入流
     * @throws Exception
     */
    public void setResponseBody(InputStream responseBody) throws Exception {
        this.responseBody = new HttpBody(
                this.header.getProperty(ResponseHeader.CONTENT_TYPE),
                responseBody
        );
    }

    /**
     * 设置响应实体
     *
     * @param responseBody 请求实体文本
     * @throws Exception
     */
    public void setResponseBody(String responseBody) throws Exception {
        this.responseBody = new HttpBody(
                this.header.getProperty(ResponseHeader.CONTENT_TYPE),
                responseBody
        );
    }


    //==============================================
    //              HttpResponse输入输出流处理
    //==============================================

    /**
     * 解析输入流，生成HttpResponse对象
     * @param responseInputStream Http响应报文输入流
     */
    private void parse(InputStream responseInputStream){

    }

    /**
     * 将HttpResponse对象写入到输出流中
     * @param outputStream 目的输出流
     */
    public void writeTo(OutputStream outputStream){

    }

    /**
     * 将HttpResponse对象解析成HTTP报文
     * @return HTTP报文字符串
     */
    @Override
    public String toString() {
        return super.toString();
    }
}
