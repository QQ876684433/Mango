package service;

import exception.ServerErrorExcetipn;
import http.core.HttpRequest;
import http.core.HttpResponse;
import http.util.HttpMethod;
import http.util.HttpStatus;
import http.util.header.RequestHeader;
import http.util.header.ResponseHeader;
import model.ModifiableFile;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        matcher.find();
        String fileName = matcher.group(1);
        ModifiableFile fileCached = ServerConfig.staticFiles.stream()
                .filter(f -> f.getName().equals(fileName))
                .findFirst()
                .orElse(null);
        String rawDate = request.getHeader().getProperty(RequestHeader.IF_MODIFIED_SINCE);
        SimpleDateFormat modifyFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");
        if (fileCached != null && rawDate != null) {
            String ifModifiedSince = rawDate.substring(0, rawDate.lastIndexOf(" "));
            //TODO 还有缓存什么的乱七八糟的东西
            try {
                Date sinceDate = modifyFormat.parse(ifModifiedSince);
                if (fileCached.getLastModifiedTime().after(sinceDate)) {
                    response.addHeader(ResponseHeader.LAST_MODIFIED, modifyFormat.format(fileCached.getLastModifiedTime()) + " GMT");
                } else {
                    response.setStatus(HttpStatus.CODE_304);
                    return;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                response.setStatus(HttpStatus.CODE_500);
            }
        }
        try {
            if (fileName.length() == 0)
                throw new FileNotFoundException();
//            InputStream inputStream = new FileInputStream(new File(ServerConfig.ROOT_PATH + "/" + fileName));
            //TODO add body length
            InputStream inputStream = FileService.class.getClassLoader().getResourceAsStream("index.html");
            response.addHeader(ResponseHeader.CONTENT_LENGTH, ""+inputStream.available());
            response.addHeader(
                    ResponseHeader.CONTENT_TYPE,
                    request.getHeader().getProperty(RequestHeader.CONTENT_TYPE));
            response.setResponseBody(inputStream);
            response.setStatus(HttpStatus.CODE_200);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("文件不存在");
            response.setStatus(HttpStatus.CODE_404);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpStatus.CODE_500);
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
