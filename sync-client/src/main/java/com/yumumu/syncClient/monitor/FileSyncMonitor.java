package com.yumumu.syncClient.monitor;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.yumumu.syncClient.service.DownloadService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhanghailin
 * @date 2022/6/7
 */
@Component
@Slf4j
public class FileSyncMonitor {

    @Resource
    private DownloadService downloadService;

    private Integer loopIndex = 1;

    public void sync() {
        Thread thread = new Thread(() -> {
            while (true) {
                if (!downloadService.download()) {
                    try {
                        Thread.sleep(5000);
                        showHeatBeatLog();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    private void showHeatBeatLog() {
        if (loopIndex++ % 12 == 0) {
            log.info("WAITING……");
            loopIndex = 1;
        }
    }
}
