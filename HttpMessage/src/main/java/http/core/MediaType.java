package http.core;

import lombok.Data;

import java.nio.charset.Charset;

/**
 * 媒体类型类
 *
 * @author steve
 */
@Data
class MediaType {
    /**
     * 主类型
     */
    private String mainType;

    /**
     * 子类型
     */
    private String subType;

    /**
     * 属性，主要是字符集
     *
     * @see java.nio.charset.Charset
     */
    private Charset charset;

    /**
     * 根据ContentType字符串解析成MediaType对象
     *
     * @param type ContentType字符串
     */
    public MediaType(String type) throws Exception {
        if (type.isEmpty()) throw new Exception("MediaType不能为空！");

        String[] split = type.split(";");

        if (split.length >= 2)
            charset = Charset.forName(split[1]);

        if (split[0].isEmpty()) throw new Exception("MediaType类型无效！");
        String[] types = split[0].split("/");
        this.setMainType(types[0]);
        if (types.length == 2) this.setSubType(types[1]);
    }
}
