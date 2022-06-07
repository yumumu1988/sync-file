package com.yumumu.syncServer.controller;

import java.io.*;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;
import com.yumumu.syncServer.model.bo.ClientInfo;
import com.yumumu.syncServer.model.bo.ResultData;

/**
 * @author zhanghailin
 * @date 2022/6/7
 */
@RestController
@RequestMapping("/download")
public class DownloadController {

    @PostMapping("/client/{clientId}")
    public ResultData getFileId(@PathVariable("clientId") String clientId, @RequestBody ClientInfo clientInfo) {

        return ResultData.success(3);
    }

    @PostMapping("/client/{clientId}/file/{fileId}")
    public void downloadFile(@PathVariable("clientId") String clientId, @PathVariable("fileId") Integer fileId,
        @RequestBody ClientInfo clientInfo, HttpServletResponse response) {
        System.out.println(JSONObject.toJSONString(clientInfo));
        // String filePath = null;
        String filePath = "D:\\test\\1223.txt";

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
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
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
