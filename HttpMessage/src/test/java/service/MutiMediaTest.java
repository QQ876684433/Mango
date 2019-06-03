package service;

import org.junit.Assert;
import org.junit.Test;
import service.fileTrans.FileTransformer;
import service.fileTrans.TextTransformer;

import java.io.*;

/**
 * 用于文件类型解析返回的测试
 */
public class MutiMediaTest {
    private FileTransformer fileTransformer;

    @Test
    public void plainTextTest() throws IOException {
        File file = new File(relativePath() + "plainText.txt");
        this.fileTransformer = new TextTransformer();
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        Assert.assertNotNull(fileTransformer);
        Assert.assertNotNull(in);
        Object res = fileTransformer.transform(in);
        Assert.assertEquals("计你太美", res);
    }

    @Test
    public void mutiTest() throws IOException {
        //TODO:实现多媒体测试
    }

    private String relativePath() throws IOException {
        String path = new File(".").getCanonicalPath();//获取当前路径
        return path + "/mutiMedia/";
    }
}
