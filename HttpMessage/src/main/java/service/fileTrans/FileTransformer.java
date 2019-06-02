package service.fileTrans;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 实现不同类型的字节流解析
 * ContentType：
 * text/plain;  //.txt
 *  charset=utf-8（文本文件）、
 * image/png（.png文件）、
 * audio/mp3（.mp3文件）、
 * video/mpeg4（.mp4文件）
 */

public interface FileTransformer {
    Object transform(InputStream in);
}
