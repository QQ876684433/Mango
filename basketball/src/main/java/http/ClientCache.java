package http;

import http.core.HttpResponse;
import http.util.header.ResponseHeader;
import lombok.Getter;
import model.CachedFile;
import util.HttpDateUtil;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * http缓存
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/6/6
 */
public class ClientCache {
    private static final String cachePath = System.getenv("appdata") + "/mango/client/cache";
    private static ClientCache instance = null;

    @Getter
    private List<CachedFile> cachedFiles = new ArrayList<>();

    private ClientCache() {
    }

    public static ClientCache getInstance() {
        if (instance == null)
            instance = new ClientCache();
        return instance;
    }


    public void cache(String uri, HttpResponse response) {

        InputStream in = response.getResponseBodyStream();
//        String file = uri.substring(0, uri.indexOf("?"));
        String file = uri;
        try {
            byte[] buffer = new byte[1024];
            OutputStream out = new FileOutputStream(cachePath + (file.startsWith("/") ? "" : "/") + file);
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }

            CachedFile cachedFile = new CachedFile();
            cachedFile.setFileName(file);
            cachedFile.setETag(response.getHeader().getProperty(ResponseHeader.ETAG));
            cachedFile.setExpires(HttpDateUtil.parse(response.getHeader().getProperty(ResponseHeader.EXPIRES)));
            cachedFile.setGainedTime(HttpDateUtil.parse(response.getHeader().getProperty(ResponseHeader.DATE)));
            cachedFile.setLastModifiedTime(HttpDateUtil.parse(response.getHeader().getProperty(ResponseHeader.LAST_MODIFIED)));
            cachedFile.setContentLocation(response.getHeader().getProperty(ResponseHeader.CONTENT_LOCATION));
            String cacheControl = response.getHeader().getProperty(ResponseHeader.CACHE_CONTROLL);
            cachedFile.setMaxAge(Integer.parseInt(cacheControl.substring(cacheControl.indexOf("=") + 1)));

            cachedFiles.add(cachedFile);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public CachedFile getCachedFile(String uri) {
        return cachedFiles.stream().filter(f -> f.getFileName().equals(uri)).findFirst().orElse(null);
    }

    public InputStream getFileInputStream(String file) throws FileNotFoundException {
        InputStream in = new FileInputStream(new File(cachePath + (file.startsWith("/") ? "" : "/") + file));
        return in;
    }

}
