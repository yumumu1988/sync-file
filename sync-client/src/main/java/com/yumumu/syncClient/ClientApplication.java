package com.yumumu.syncClient;

import com.yumumu.syncClient.monitor.FileSyncMonitor;
import com.yumumu.syncClient.monitor.FileWatcherMonitor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author zhanghailin
 * @date 2022/6/7
 */
@SpringBootApplication
public class ClientApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ClientApplication.class, args);
        FileWatcherMonitor monitor = context.getBean(FileWatcherMonitor.class);
        FileSyncMonitor syncMonitor = context.getBean(FileSyncMonitor.class);
        try {
            monitor.watch();
            syncMonitor.sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
