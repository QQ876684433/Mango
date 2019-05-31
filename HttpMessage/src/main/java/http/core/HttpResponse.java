package http.core;

import com.sun.istack.internal.Nullable;
import http.util.HttpStatus;
import http.util.header.RequestHeader;
import http.util.header.ResponseHeader;
import http.util.io.InputOutputTransform;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.*;
import java.nio.charset.Charset;

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
    private String version;

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
        // 解析响应报文起始行
        int buffer;

        try {
            byte[] bf = new byte[responseInputStream.available()];
            int pointer = 0;
            while ((buffer = responseInputStream.read()) != '\n')
                bf[pointer++] = (byte) buffer;
            String[] splits = new String(bf, Charset.defaultCharset()).split(" ");
            this.setVersion(splits[0]);
            this.setStatus(Integer.parseInt(splits[1]));
            this.setVersion(splits[2]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 解析响应报文首部
        this.header = new Header(responseInputStream);

        // 解析响应报文实体部分
        try {
            this.responseBody = new HttpBody(
                    this.header.getProperty(RequestHeader.CONTENT_TYPE),
                    responseInputStream
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将HttpResponse对象写入到输出流中
     * @param outputStream 目的输出流
     */
    public void writeTo(OutputStream outputStream){
        PrintWriter pw = new PrintWriter(outputStream);

        // 输出请求报文起始行
        pw.print(this.getVersion() + " ");
        pw.print(this.getStatus() + " ");
        pw.println(this.getMessage());

        // 输出请求首部
        String headers = this.getHeader().getHeaderText(this.responseBody.getMediaType().getCharset());
        pw.println(headers);

        // 输出空行
        pw.println();

        // 处理请求实体
        if (this.responseBody != null){
            InputStream is = responseBody.getContent();
            try {
                InputOutputTransform.inputStream2OutputStream(is, outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将HttpResponse对象解析成HTTP报文
     * @return HTTP报文字符串
     */
    @Override
    public String toString() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        this.writeTo(os);
        return os.toString();
    }
}
