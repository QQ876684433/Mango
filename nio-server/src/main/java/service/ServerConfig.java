package service;

import model.ModifiableFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 服务器的相关配置信息
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/6/5
 */
public class ServerConfig {
    static final String ROOT_PATH = System.getenv("appdata") + "/mango/server/file";
    static final List<ModifiableFile> staticFiles = new ArrayList<>();

    static {
        staticFiles.add(new ModifiableFile("index.html", new Date()));
        staticFiles.add(new ModifiableFile("file1.txt", new Date()));
    }
}

