package com.yumumu.syncServer.controller;

import java.io.*;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.yumumu.syncServer.model.bo.ClientInfo;
import com.yumumu.syncServer.model.bo.DownloadFileInfo;
import com.yumumu.syncServer.model.bo.ResultData;
import com.yumumu.syncServer.model.po.FileRecord;
import com.yumumu.syncServer.service.ClientsService;
import com.yumumu.syncServer.service.FileRecordService;
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

    @Resource
    private FileRecordService fileRecordService;

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

        String filePath = fileDir + tempName;
        File file = new File(filePath);
        if (file.exists()) {
            // ????????????????????????????????????pdf??????
            // ??????Content-Disposition?????????attachment???????????????????????????????????????filename???????????????????????????????????????
            // ?????????Content-Disposition????????????????????????????????????????????????txt???img??????
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

    @GetMapping("/file/{tempName}")
    public void downloadFile(@PathVariable("tempName") String tempName, HttpServletRequest httpServletRequest,
        HttpServletResponse response) throws IOException {
        if (!AccessUtils.access(httpServletRequest)) {
            response.sendRedirect("/");
            return;
        }
        String filePath = fileDir + tempName;
        File file = new File(filePath);
        if (file.exists()) {
            FileRecord fileRecord = fileRecordService.getFileRecordByTempName(tempName);
            // ????????????????????????????????????pdf??????
            // ??????Content-Disposition?????????attachment???????????????????????????????????????filename???????????????????????????????????????
            // ?????????Content-Disposition????????????????????????????????????????????????txt???img??????
            response.addHeader("Content-Disposition", "attachment; filename=" + fileRecord.getFilename());
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            // if using Java 7, use try-with-resources
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                log.info("sending file " + tempName);
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                log.info(tempName + " has been sent");
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

    @GetMapping("/all")
    public ResultData<List<FileRecord>> getAll(HttpServletRequest httpServletRequest, HttpServletResponse response)
        throws IOException {
        if (!AccessUtils.access(httpServletRequest)) {
            response.sendRedirect("/");
            return null;
        }
        try {
            return ResultData.success(fileRecordService.getAllFileList());
        } catch (Exception e) {
            return ResultData.failed(e.getMessage());
        }
    }

}
