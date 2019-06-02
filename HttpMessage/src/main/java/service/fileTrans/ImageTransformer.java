package service.fileTrans;

import java.io.InputStream;

//javafx提供Image API,给予inputStream直接展示图片
public class ImageTransformer implements FileTransformer {
    @Override
    public Object transform(InputStream in) {
        return in;
    }
}
