package http.core;

import lombok.Getter;

import java.io.*;

/**
 * HTTP报文实体类
 *
 * @author steve
 */
public class HttpBody {
    /**
     * 媒体类型
     */
    @Getter
    private MediaType mediaType;

    /**
     * 实体部分
     */
    private byte[] content;

    HttpBody(String contentType) throws Exception {
        this(contentType, "");
    }

    HttpBody(String contentType, String content) throws Exception {
        if (contentType.isEmpty()) throw new Exception("contentType不能为空！");

        this.mediaType = new MediaType(contentType);
        this.setContent(new ByteArrayInputStream(content.getBytes(this.mediaType.getCharset())));
    }

    HttpBody(String contentType, InputStream content) throws Exception {
//        if (contentType.isEmpty()) throw new Exception("contentType不能为空！");
        if (null == contentType) contentType = "text/plain; charset=utf-8";

        this.mediaType = new MediaType(contentType);
        this.setContent(content);
    }

    /**
     * 获取输出流的拷贝
     *
     * @return 输出流
     */
    public InputStream getContent() {
        return new ByteArrayInputStream(this.content);
    }

    /**
     * 设置实体内容，将输入流转换为byte数组以便复用
     *
     * @param is
     */
    void setContent(InputStream is) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[is.available()];
            int len = -1;
            len = is.read(buffer);
//            while ((len = is.read(buffer)) != -1) {
            output.write(buffer, 0, len);
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.content = output.toByteArray();
    }

    /**
     * 获取HTTP报文实体的文本内容
     *
     * @return HTTP报文实体文本内容
     */
    String getTextContent() {
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(this.getContent()));
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
