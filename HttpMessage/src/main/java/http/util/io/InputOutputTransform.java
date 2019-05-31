package http.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 输入输出流的转换
 *
 * @author steve
 */
public class InputOutputTransform {

    /**
     * 将输入流转为输出流
     *
     * @param is 输入流
     * @param os 输出流
     */
    public static void InputStream2OutputStream(InputStream is, OutputStream os) throws IOException {
        byte[] buffer = new byte[1024];
        while ((is.read(buffer)) != 0) {
            os.write(buffer);
        }
    }

    /**
     * 将输出流转为输入流
     *
     * @param is 输入流
     * @param os 输出流
     */
    static void OutputStream2InputStream(InputStream is, OutputStream os) {
        // can do nothing
    }
}
