package com.yumumu.syncClient.monitor;

import com.yumumu.syncClient.service.DownloadService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author zhanghailin
 * @date 2022/6/7
 */
@Component
public class FileSyncMonitor {

    @Resource
    private DownloadService downloadService;

    public void sync() {
        Thread thread = new Thread(() -> {
            while (true) {
                if (!downloadService.download()) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

}
