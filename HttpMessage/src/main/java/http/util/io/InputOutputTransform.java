package http.util.io;

import java.io.*;

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
    public static void inputStream2OutputStream(InputStream is, OutputStream os) throws IOException {
        byte[] buffer = new byte[1024];
        while ((is.read(buffer)) != 0) {
            os.write(buffer);
        }
    }

    /**
     * 将输出流转为输入流
     *
     * @param br 字符输入流
     * @return 字节输入流
     */
    public static InputStream bufferedReader2InputStream(BufferedReader br) {
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while (!(line = br.readLine()).isEmpty()) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(sb.toString().getBytes());
    }
}
