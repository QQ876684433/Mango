package service.fileTrans;

import service.FileUtils;

import java.io.InputStream;

public class AudioTransformer implements FileTransformer {
    @Override
    public Object transform(InputStream in) {
        return FileUtils.streamToString(in, "utf-8");

    }
}
