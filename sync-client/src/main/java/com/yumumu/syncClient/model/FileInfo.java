package com.yumumu.syncClient.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.io.*;

/**
 * @author zhanghailin
 * @date 2022/6/7
 */
public class FileInfo implements Serializable {
    private static final long serialVersionUID = 4177148237618974107L;

    @Getter
    private String filePath;
    private String fileName;
    private String fileMd5;
    @Getter
    private File file;
    @Getter
    @Setter
    private String relativeFilePath;

    public FileInfo(String filePath) {
        this.file = new File(filePath);
        assert this.file.isFile() && this.file.exists();
        this.filePath = filePath;
    }

    public FileInfo(File file) {
        this.file = file;
        this.filePath = file.getAbsolutePath();
    }

    public FileInfo(File file, String relativeFilePath) {
        this(file);
        this.relativeFilePath = relativeFilePath;
    }

    public String getFileMd5() {
        if (StringUtils.isEmpty(this.fileMd5)) {
            try (FileInputStream fileInputStream = new FileInputStream(filePath)){
                this.fileMd5 = DigestUtils.md5DigestAsHex(fileInputStream);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return this.fileMd5;
    }

    public String getFileName() {
        if (StringUtils.isEmpty(this.fileName)) {
            this.fileName = this.file.getName();
        }
        return this.fileName;
    }

}
