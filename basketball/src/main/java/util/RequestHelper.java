package util;

import http.util.header.RequestHeader;
import model.ParamTuple;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 围绕着Request进行各种操作的辅助类
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/6/3
 */
public class RequestHelper {

    /**
     * 验证传入的参数(ParamTuple实例)是否符合header的格式
     *
     * @param o 待验证的ParamTuple实例
     * @return 如果符合http规范的header形式，返回true，否则返回false
     */
    public static boolean validateHeader(ParamTuple o) {
        Class<RequestHeader> clazz = RequestHeader.class;
        Field[] fields = clazz.getFields();

        List<String> headers = Arrays.stream(fields).map(field -> {
            try {
                return (String) field.get(clazz);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        return headers.contains(o.getKey()) && o.getValue().length() > 0;
    }

    /**
     * 验证传入的参数(ParamTuple实例)是否符合query_parameter的格式
     *
     * @param o 待验证的ParamTuple实例
     * @return 如果符合http规范的query_parameter形式，返回true，否则返回false
     */
    public static boolean validateParam(ParamTuple o) {
        return o.getKey().length() > 0 && o.getValue().length() > 0;
    }

    /**
     * 将请求参数转换为一份字符串
     *
     * @param list 参数列表，以{@link ParamTuple}为信息载体
     * @return 构建好的参数字符串
     */
    public static String buildParamString(List<ParamTuple> list) {
        StringBuilder builder = new StringBuilder();
        list.stream().filter(RequestHelper::validateParam)
                .forEach(o -> {
                    if (builder.length() > 1)
                        builder.append("&");
                    builder.append(o.getKey()).append("=").append(o.getValue());
                });

        return builder.toString();
    }
}
