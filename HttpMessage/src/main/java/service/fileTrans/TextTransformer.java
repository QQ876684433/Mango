package service.fileTrans;

import service.FileUtils;

import java.io.InputStream;

/**
 * 文本文件
 * */
public class TextTransformer implements FileTransformer {

    @Override
    public Object transform(InputStream in) {
        return FileUtils.streamToString(in, "utf-8");
    }
}
