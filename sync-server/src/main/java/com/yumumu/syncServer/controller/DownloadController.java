package com.yumumu.syncServer.controller;

import java.io.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.yumumu.syncServer.model.bo.ClientInfo;
import com.yumumu.syncServer.model.bo.DownloadFileInfo;
import com.yumumu.syncServer.model.bo.ResultData;
import com.yumumu.syncServer.service.ClientsService;
import com.yumumu.syncServer.utils.AccessUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhanghailin
 * @date 2022/6/7
 */
@RestController
@RequestMapping("/download")
@Slf4j
public class DownloadController {

    @Value("${file.dir}")
    private String fileDir;

    @Resource
    private ClientsService clientsService;

    @PostMapping("/client/{clientId}")
    public ResultData getFileId(@PathVariable("clientId") String clientId, @RequestBody ClientInfo clientInfo) {
        if (!AccessUtils.access(clientInfo)) {
            return ResultData.failed("ACCESS FAILED");
        }
        DownloadFileInfo downloadFileInfo = clientsService.getDownloadFileInfo(clientId);
        if (!StringUtils.isEmpty(downloadFileInfo.getTempName())) {
            return ResultData.success(downloadFileInfo);
        }
        return ResultData.failed("NO FILES");
    }

    @PostMapping("/client/{clientId}/file/{tempName}")
    public void downloadFile(@PathVariable("clientId") String clientId, @PathVariable("tempName") String tempName,
        @RequestBody ClientInfo clientInfo, HttpServletResponse response) {
        if (!AccessUtils.access(clientInfo)) {
            return;
        }

        // String filePath = null;
        String filePath = fileDir + tempName;

        File file = new File(filePath);
        if (file.exists()) {
            // 设置响应类型，这里是下载pdf文件
            // 设置Content-Disposition，设置attachment，浏览器会激活文件下载框；filename指定下载后默认保存的文件名
            // 不设置Content-Disposition的话，文件会在浏览器内打卡，比如txt、img文件
            response.addHeader("Content-Disposition", "attachment; filename=" + file.getName());
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            // if using Java 7, use try-with-resources
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                log.info("sending file " + tempName + " to " + clientId);
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                log.info(tempName + " has been sent to " + clientId);
                clientsService.updateFileIndex(clientId, tempName);
            } catch (IOException ex) {
                // do something,
                // probably forward to an Error page
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
    }

}
