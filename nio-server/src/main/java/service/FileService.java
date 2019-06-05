package service;

import http.core.HttpRequest;
import http.core.HttpResponse;
import http.util.HttpMethod;
import http.util.HttpStatus;
import http.util.header.RequestHeader;
import http.util.header.ResponseHeader;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 提供文件服务
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/6/5
 */
public class FileService implements ServerService {

    private static FileService instance;

    private FileService() {

    }

    public static FileService getInstance() {
        if (instance == null) {
            instance = new FileService();
        }
        return instance;
    }

    @Override
    public HttpResponse processRequest(HttpRequest request) {
        HttpResponse response = new HttpResponse();
        if (request.getMethod().equals(HttpMethod.GET))
            getFile(request, response);
        else if (request.getMethod().equals(HttpMethod.POST))
            postFile(request, response);
        else {
            //方法不符合
            response.setStatus(HttpStatus.CODE_405);
            response.addHeader(ResponseHeader.ALLOW, "GET, POST");
        }
        return response;


    }

    // GET url = '/file/xxx.xx'
    private void getFile(HttpRequest request, HttpResponse response) {
        String url = request.getUrl();
        String pattern = "/file/(.+[.].+)";
        Matcher matcher = Pattern.compile(pattern).matcher(url);
        String fileName = matcher.group();
        try {
            if (fileName.length() == 0)
                throw new FileNotFoundException();
            InputStream inputStream = new FileInputStream(new File(ServerConfig.ROOT_PATH + "/" + fileName));
            response.setResponseBody(inputStream);
            response.addHeader(
                    ResponseHeader.CONTENT_TYPE,
                    request.getHeader().getProperty(RequestHeader.CONTENT_TYPE));
            //TODO add body length
            response.addHeader(ResponseHeader.CONTENT_LENGTH, "xxx");
            response.setStatus(HttpStatus.CODE_200);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("文件不存在");
            response.setStatus(HttpStatus.CODE_404);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // POST url = '/file?file_name=xxx.xx'
    private void postFile(HttpRequest request, HttpResponse response) {
        String url = request.getUrl();
        String fileName = url.substring(url.indexOf("=") + 1);
        try {
            byte[] buffer = new byte[1024];
            InputStream in = request.getRequestBodyStream();
            OutputStream out = new FileOutputStream(ServerConfig.ROOT_PATH + "/" + fileName);
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            response.setStatus(HttpStatus.CODE_200);

        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(HttpStatus.CODE_500);
        }
    }
}
