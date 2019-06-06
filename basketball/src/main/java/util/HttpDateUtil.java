package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 用于http头部的日期转换类
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/6/6
 */
public class HttpDateUtil {
    private static final SimpleDateFormat format = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss 'GMT'");

    /**
     * 从源字符串获取日期
     * 源字符串格式: E, dd MMM yyyy HH:mm:ss GMT
     *
     * @param source 待解析的字符串
     * @return 解析成功的日期
     */
    public static Date parse(String source) throws ParseException {
        return format.parse(source);
    }

    /**
     * 将日期转换为http使用标准格式的字符串
     *
     * @param date 待格式化的日期
     * @return 格式化成功的字符串
     */
    public static String format(Date date) {
        return format.format(date);
    }
}
