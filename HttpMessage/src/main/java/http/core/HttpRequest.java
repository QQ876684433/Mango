package http.core;

import com.sun.istack.internal.Nullable;
import http.exception.HttpParseFailException;
import http.exception.HttpWriteOutException;
import http.util.HttpMethod;
import http.util.header.RequestHeader;
import http.util.io.InputOutputTransform;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.*;
import java.nio.charset.Charset;

/**
 * HTTP请求报文类
 *
 * @author steve
 */
public class HttpRequest {
    /**
     * HTTP请求方式
     *
     * @see http.util.HttpMethod
     */
    @Getter
    @Setter
    @NonNull
    private String method;

    /**
     * HTTP请求路径
     */
    @Getter
    @Setter
    @NonNull
    private String url;

    /**
     * HTTP请求报文使用的HTTP协议版本
     *
     * @see http.util.HttpVersion
     */
    @Getter
    @Setter
    @NonNull
    private String version;

    /**
     * HTTP请求报文首部
     */
    @Getter
    @NonNull
    private Header header;

    /**
     * HTTP请求实体
     */
    @Nullable
    private HttpBody requestBody = null;

    public HttpRequest() {
        header = new Header();
    }

    public HttpRequest(InputStream requestInputStream) throws Exception {
        this();
        this.parse(requestInputStream);
    }


    //==============================================
    //              请求首部设置
    //==============================================

    /**
     * 支持链式调用
     *
     * @param key   首部键
     * @param value 对应key的属性值
     * @return 返回HttpRequest实例本身，支持链式调用
     */
    public HttpRequest setHeader(String key, String value) {
        header.setProperty(key, value);
        return this;
    }


    //==============================================
    //              响应实体
    //==============================================

    /**
     * 获取实体部分文本内容
     *
     * @return 请求实体文本内容
     */
    public String getRequestBodyText() {
        if (this.requestBody == null) throw new NullPointerException("当前请求实体为空！");
        return requestBody.getTextContent();
    }

    /**
     * 获取实体部分输入流
     *
     * @return 请求实体输入流
     */
    public InputStream getRequestBodyStream() {
        if (this.requestBody == null) throw new NullPointerException("当前请求实体为空！");
        return requestBody.getContent();
    }

    /**
     * 设置请求实体
     *
     * @param requestBody 请求实体输入流
     * @throws Exception
     */
    public void setRequestBody(InputStream requestBody) throws Exception {
        if (this.method.equalsIgnoreCase(HttpMethod.GET))
            throw new Exception("GET请求不能设置请求实体！");

        this.requestBody = new HttpBody(
                this.header.getProperty(RequestHeader.CONTENT_TYPE),
                requestBody
        );
    }

    /**
     * 设置请求实体
     *
     * @param requestBody 请求实体文本
     * @throws Exception
     */
    public void setRequestBody(String requestBody) throws Exception {
        if (this.method.equalsIgnoreCase(HttpMethod.GET))
            throw new Exception("GET请求不能设置请求实体！");

        this.requestBody = new HttpBody(
                this.header.getProperty(RequestHeader.CONTENT_TYPE),
                requestBody
        );
    }


    //==============================================
    //              HttpResponse输入输出流处理
    //==============================================

    /**
     * 解析请求报文输入流，构建HttpRequest对象
     *
     * @param requestInputStream 请求报文输入流
     */
    private void parse(InputStream requestInputStream) throws Exception {
        int buffer;

        // 解析请求报文起始行
        try {
            byte[] bf = new byte[requestInputStream.available()];
            int pointer = 0;
            while ((buffer = requestInputStream.read()) != '\n')
                bf[pointer++] = (byte) buffer;
            String[] splits = new String(bf, Charset.defaultCharset()).trim().split(" ");
            this.setMethod(splits[0]);
            this.setUrl(splits[1]);
            this.setVersion(splits[2]);
        } catch (Exception e) {
            throw new HttpParseFailException("解析请求报文起始行出错！");
        }

        // 解析请求报文首部
        this.header = new Header(requestInputStream);

        // 解析请求报文实体部分（GET请求不解析实体部分）
        if (!this.getMethod().equalsIgnoreCase(HttpMethod.GET))
            try {
                this.requestBody = new HttpBody(
                        this.header.getProperty(RequestHeader.CONTENT_TYPE),
                        requestInputStream
                );
            } catch (Exception e) {
                throw new HttpParseFailException("解析实体部分出错！");
            }
    }

    /**
     * 将HttpRequest对象写入到输出流中
     *
     * @param outputStream 目的输出流
     */
    public void writeTo(OutputStream outputStream) throws HttpWriteOutException {
        PrintWriter pw = new PrintWriter(outputStream);

        // 输出请求报文起始行
        pw.print(this.getMethod() + " ");
        pw.print(this.getUrl() + " ");
        pw.println(this.getVersion());

        // 输出请求首部
        String headers = this.getHeader().getHeaderText();
        pw.print(headers);

        // 输出空行
        pw.println();

        // 处理请求实体
        if (this.requestBody != null) {
            InputStream is = requestBody.getContent();
            try {
                byte[] buf = new byte[is.available()];
                is.read(buf);
                pw.write(new String(buf));
            } catch (IOException e) {
                throw new HttpWriteOutException("请求报文发送失败！");
            }
//            try {
//                InputOutputTransform.inputStream2OutputStream(is, outputStream);
//            } catch (IOException e) {
//                throw new HttpWriteOutException("请求报文发送失败！");
//            }
        }
        pw.flush();
    }

    /**
     * 将HttpRequest对象解析成HTTP报文
     *
     * @return HTTP报文字符串
     */
    @Override
    public String toString() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            this.writeTo(os);
        } catch (HttpWriteOutException e) {
            e.printStackTrace();
        }
        return os.toString();
    }
}
