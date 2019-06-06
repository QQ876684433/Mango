package model;

import lombok.Data;

import java.util.Date;

/**
 * 用于CODE_304相关的信息类
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/6/6
 */
@Data
public class ModifiableFile {
    private String name;
    private Date lastModifiedTime;

    public ModifiableFile() {
    }

    public ModifiableFile(String name, Date lastModifiedTime) {
        this.name = name;
        this.lastModifiedTime = lastModifiedTime;
    }
}
