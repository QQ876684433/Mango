package util;

import model.DataStore;
import model.ParamTuple;
import org.apache.commons.lang3.ArrayUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * 获取请求content所需的辅助类
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/6/7
 */
public class RequestContentHelper {

    public static String getContentType() {
        switch (DataStore.getBodySelectedBtn()[0]) {
            case 2:
                return "multipart/form-data";
            case 3:
                return "application/x-www-form-urlencoded";
            case 4:
                String rawType = DataStore.getRawType();
                if ("Text".equals(rawType)) {
                    return "application/x-www-form-urlencoded";
                } else {
                    return rawType.substring(rawType.indexOf("(") + 1, rawType.indexOf(")"));
                }
            case 1:
            case 5:
                return "text/plain";
            default:
                return "";
        }
    }


    public static byte[] getContent() {
        switch (DataStore.getBodySelectedBtn()[0]) {
            case 2:
                //TODO 未实现同时上传文本与文件
                List<ParamTuple> formUnits = DataStore.getFormData();
                return paramTuple2Bytes(formUnits);
            case 3:
                List<ParamTuple> rows = DataStore.getUrlEncoded();
                return paramTuple2Bytes(rows);
            case 4:
                return DataStore.getRaw().getBytes();
            case 5:
                byte[] buffer = null;

                try {
                    File file = new File(DataStore.getFilePath());
                    FileInputStream fis = new FileInputStream(file);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
                    byte[] content = new byte[1000];
                    int len;
                    while ((len = fis.read(content)) != -1) {
                        bos.write(content, 0, len);
                    }
                    fis.close();
                    bos.close();
                    buffer = bos.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return buffer;
            default:
                return new byte[0];
        }

    }

    private static byte[] paramTuple2Bytes(List<ParamTuple> list) {
        final byte[][] bytes = {new byte[0]};
        list.forEach(t -> {
            bytes[0] = ArrayUtils.addAll(bytes[0], (t.getKey() + "=" + t.getValue()).getBytes());
        });
        return bytes[0];
    }
}
