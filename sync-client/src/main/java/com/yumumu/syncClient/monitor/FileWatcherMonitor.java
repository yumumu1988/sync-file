package com.yumumu.syncClient.monitor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.yumumu.syncClient.listener.FileListener;

@Component
public class FileWatcherMonitor {

    private static final Logger log = LoggerFactory.getLogger(FileWatcherMonitor.class);
    @Autowired
    private FileListener fileListener;
    @Value("${file.watcher.path}")
    private String watchPath;

    public void watch() throws Exception {

        // 监控目录
        String rootDir = watchPath;
        // 轮询间隔 5 秒
        long interval = TimeUnit.MILLISECONDS.toMillis(500);
        // 创建过滤器
        IOFileFilter directories = FileFilterUtils.and(FileFilterUtils.directoryFileFilter(), HiddenFileFilter.VISIBLE);
        IOFileFilter files =
            FileFilterUtils.and(FileFilterUtils.fileFileFilter(), FileFilterUtils.suffixFileFilter(".DS_Store"));
        // IOFileFilter filter = FileFilterUtils.or(directories, files);
        IOFileFilter filter = FileFilterUtils.notFileFilter(files);
        log.info("MONITOR PATH: " + watchPath);
        // 使用过滤器
        // FileAlterationObserver observer = new FileAlterationObserver(new File(rootDir), filter);
        // 不使用过滤器

        FileAlterationObserver observer = new FileAlterationObserver(new File(rootDir), filter);
        observer.addListener(fileListener);
        // 创建文件变化监听器
        FileAlterationMonitor monitor = new FileAlterationMonitor(interval, observer);
        // 开始监控
        monitor.start();
    }

}
