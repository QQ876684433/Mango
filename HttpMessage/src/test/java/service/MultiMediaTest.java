package service;

import jdk.internal.util.xml.impl.Input;
import org.junit.Assert;
import org.junit.Test;
import service.fileTrans.FileTransformer;
import service.fileTrans.ImageTransformer;
import service.fileTrans.TextTransformer;

import java.io.*;

/**
 * 用于文件类型解析返回的测试
 */
public class MultiMediaTest {
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
    public void multiTest() throws IOException {
        //TODO:实现多媒体测试
        File file = new File(relativePath() + "iii.jpg");
        this.fileTransformer = new ImageTransformer();
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        Assert.assertNotNull(this.fileTransformer);
        Assert.assertNotNull(in);
        Object res = fileTransformer.transform(in);
        res = (InputStream) res;
        byte[] bytes = new byte[1024];
        ((InputStream) res).read(bytes);
        System.out.println(bytes);
    }

    private String relativePath() throws IOException {
        String path = new File(".").getCanonicalPath();//获取当前路径
        return path + "/mutiMedia/";
    }
}
