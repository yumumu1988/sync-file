package com.yumumu.syncClient.listener;

import com.yumumu.syncClient.model.FileInfo;
import com.yumumu.syncClient.service.UploadService;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;

/**
 * @author zhanghailin
 * @date 2022/6/7
 */
@Component
public class FileListener extends FileAlterationListenerAdaptor {

    @Value("${file.watcher.path}")
    private String watchPath;

    @Resource
    private UploadService uploadService;

    @Override
    public void onFileCreate(File file) {
        System.out.println("create file " + file.getAbsolutePath());
        uploadService.uploadFile(new FileInfo(file, getRelativePath(file)));
    }

    private String getRelativePath(File file) {
        String path = file.getAbsolutePath().replace(watchPath, "");
        return path.replace(file.getName(), "");
    }

    @Override
    public void onFileChange(File file) {
        System.out.println("file changed " + file.getAbsolutePath());
        uploadService.uploadFile(new FileInfo(file, getRelativePath(file)));
    }
}
