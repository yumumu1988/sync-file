package com.yumumu.syncServer.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.yumumu.syncServer.model.bo.ClientInfo;
import com.yumumu.syncServer.model.bo.ResultData;
import com.yumumu.syncServer.service.FileRecordService;
import com.yumumu.syncServer.utils.TokenUtils;

/**
 * @author zhanghailin
 * @date 2022/6/7
 */
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Resource
    private FileRecordService fileRecordService;

    @PostMapping("/file")
    public ResultData uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("token") String token) {
        File tempFile = new File("D:\\" + UUID.randomUUID().toString() + file.getContentType().split("/")[1]);
        try {
            Files.write(Paths.get(tempFile.getAbsolutePath()), file.getBytes(), StandardOpenOption.CREATE);
            String md5;
            try (FileInputStream fileInputStream = new FileInputStream(tempFile)) {
                md5 = DigestUtils.md5DigestAsHex(fileInputStream);
            } catch (IOException e) {
                e.printStackTrace();
                return ResultData.failed(e.getMessage());
            }
            if (!TokenUtils.checkToken(md5, file.getOriginalFilename(), token)) {
                tempFile.delete();
            } else {
                fileRecordService.addNewFile(file.getOriginalFilename(), tempFile.getName(), file.getSize(), md5);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultData.success(file.getOriginalFilename());
    }

    @PostMapping("/token")
    public ResultData<String> getToken(@RequestBody ClientInfo clientInfo) {
        return ResultData.success(TokenUtils.getToken(clientInfo.getFileMd5(), clientInfo.getFileName()));
    }

}
