package model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 保存body_tab相关数据的类
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/6/7
 */
public class DataStore {
    @Getter
    public static final int[] bodySelectedBtn = new int[1];
    @Getter
    public static final List<ParamTuple> formData = new ArrayList<>();
    @Getter
    public static final List<ParamTuple> urlEncoded = new ArrayList<>();
    @Getter
    @Setter
    public static String raw;
    @Getter
    @Setter
    public static String rawType;
    @Getter
    @Setter
    public static String filePath;
}
