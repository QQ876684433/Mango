package model;

import lombok.Data;

import java.util.Date;

/**
 * 保存缓存文件相关的信息
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/6/6
 */
@Data
public class CachedFile {
    private String fileName;
    private Date lastModifiedTime;
    private int maxAge;
    private Date gainedTime;
    private String contentLocation;

    public boolean isExpired() {
        Date now = new Date();
        return gainedTime.getTime() + maxAge * 10 < now.getTime();
    }
}
