package service;

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
//            InputStream inputStream = FileService.class.getClassLoader().getResourceAsStream(fileName);
            InputStream inputStream = new FileInputStream(System.getProperty("user.dir") + "/server/" + fileName);
            response.addHeader(ResponseHeader.CONTENT_LENGTH, "" + inputStream.available());
            // TODO add content-type
            setContentType(response, fileName);
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

    // 根据文件名设置ContentType
    private void setContentType(HttpResponse response, String fileName) {
        if (fileName.endsWith(".jpg") ||
                fileName.endsWith(".png")) {
            response.addHeader(ResponseHeader.CONTENT_TYPE, "image/png");
        } else if (fileName.startsWith("musics")) {
            response.addHeader(ResponseHeader.CONTENT_TYPE, "audio/mp3");
        } else {
            response.addHeader(ResponseHeader.CONTENT_TYPE, "text/plain");
        }
    }

    // POST url = '/file?file_name=xxx.xx'
    private void postFile(HttpRequest request, HttpResponse response) {
        String url = request.getUrl();
        String fileName = url.substring(url.indexOf("=") + 1);
        String postfix;
        String pattern = ".+/(.+)";
        Matcher matcher = Pattern.compile(pattern).matcher(request.getHeader().getProperty(RequestHeader.CONTENT_TYPE));
        matcher.find();
        String type = matcher.group(1);
        switch (type) {
            case "javascript":
                postfix = ".js";
                break;
            case "xml":
            case "html":
            case "json":
                postfix = "." + type;
                break;
            case "plain":
            case "x-www-form-urlencoded":
            default:
                postfix = ".txt";
                break;
        }

        try {
            InputStream in = request.getRequestBodyStream();
            OutputStream out = new FileOutputStream(System.getProperty("user.dir") + "/server/" + fileName + postfix);
            byte[] buffer = new byte[in.available()];
//            int len;
//            while ((len = in.read(buffer)) != -1) {
//                out.write(buffer, 0, len);
//            }
            in.read(buffer);
            out.write(buffer);
            response.setStatus(HttpStatus.CODE_200);

        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(HttpStatus.CODE_500);
        }
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
    }
}
