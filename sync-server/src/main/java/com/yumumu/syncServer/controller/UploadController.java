package com.yumumu.syncServer.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.yumumu.syncServer.model.bo.ClientInfo;
import com.yumumu.syncServer.model.bo.ResultData;
import com.yumumu.syncServer.service.FileRecordService;
import com.yumumu.syncServer.utils.AccessUtils;
import com.yumumu.syncServer.utils.TokenUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhanghailin
 * @date 2022/6/7
 */
@RestController
@RequestMapping("/upload")
@Slf4j
public class UploadController {

    @Value("${file.dir}")
    private String fileDir;

    @Resource
    private FileRecordService fileRecordService;

    @PostMapping("/file")
    public ResultData uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("token") String token,
        @RequestParam("clientId") String clientId, @RequestParam("relativePath") String relativePath) {
        String[] fileInfoArray = file.getOriginalFilename().split("\\.");
        File tempFile = new File(fileDir + relativePath + UUID.randomUUID()
            + (fileInfoArray.length > 1 ? ("." + fileInfoArray[fileInfoArray.length - 1]) : ""));
        try {
            File dir = new File(fileDir + relativePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            log.info("receiving file " + file.getOriginalFilename());
            Files.write(Paths.get(tempFile.getAbsolutePath()), file.getBytes(), StandardOpenOption.CREATE);
            String md5;
            try (FileInputStream fileInputStream = new FileInputStream(tempFile)) {
                md5 = DigestUtils.md5DigestAsHex(fileInputStream);
                if (!TokenUtils.checkToken(md5, file.getOriginalFilename(), clientId, token)) {
                    tempFile.delete();
                } else {
                    fileRecordService.addNewFile(relativePath + file.getOriginalFilename(), tempFile.getName(), file.getSize(), clientId,
                            md5);
                    log.info(file.getOriginalFilename() + " has been saved");
                    File targetFile = new File(fileDir + relativePath + file.getOriginalFilename());
                    if (targetFile.exists()) {
                        targetFile.delete();
                        log.info(file.getOriginalFilename() + " has been deleted");
                    }
                    boolean rename = tempFile.renameTo(targetFile);
                    log.info("rename: " + rename);
                }
            }
        } catch (IOException e) {
            log.info("upload file failed", e);
        }
        return ResultData.success(file.getOriginalFilename());
    }

    @PostMapping("/token")
    public ResultData<String> getToken(@RequestBody ClientInfo clientInfo) {
        if (!AccessUtils.access(clientInfo)) {
            return ResultData.failed("ACCESS FAILED");
        }
        // 校验重复文件
        if (fileRecordService.isExisted(clientInfo.getFileMd5(), clientInfo.getFileName())) {
            return ResultData.failed("文件已存在");
        }
        return ResultData
            .success(TokenUtils.getToken(clientInfo.getFileMd5(), clientInfo.getFileName(), clientInfo.getClientId()));
    }

}
