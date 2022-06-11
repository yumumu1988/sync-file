package com.yumumu.syncClient.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.yumumu.syncClient.model.DownloadFileInfo;
import com.yumumu.syncClient.model.res.DownloadFileInfoResponse;
import com.yumumu.syncClient.utils.HttpClientUtil;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

/**
 * @author zhanghailin
 * @date 2022/6/7
 */
@Service
@Slf4j
public class DownloadService {

    @Value("${client.id}")
    private String clientId;
    @Value("${file.dir}")
    private String fileDir;
    @Value("${upload.ak}")
    private String ak;
    @Value("${upload.sk}")
    private String sk;
    @Value("${remote.url}")
    private String remoteUrl;

    public boolean download() {
        DownloadFileInfo downloadFileInfo = getDownloadFileInfo();
        if (!StringUtils.isEmpty(downloadFileInfo.getFileName())) {
            // 下载
            OkHttpClient okHttpClient = HttpClientUtil.getClient();
            Request request = createOneRequest(
                remoteUrl + "/download/client/" + clientId + "/file/" + downloadFileInfo.getTempName());
            Response response = null;
            try {
                log.info("downloading file " + downloadFileInfo.getFileName());
                response = okHttpClient.newCall(request).execute();
                byte[] bytes = response.body().bytes();
                Files.write(Paths.get(fileDir + downloadFileInfo.getFileName()), bytes, StandardOpenOption.CREATE);
                log.info(downloadFileInfo.getFileName() + " has been downloaded");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                return false;
            } finally {
                if (response != null) {
                    response.close();
                }
            }
            // 转化成byte数组
        } else {
            return false;
        }
    }

    private DownloadFileInfo getDownloadFileInfo() {
        OkHttpClient okHttpClient = HttpClientUtil.getClient();
        Request request = createOneRequest(remoteUrl + "/download/client/" + clientId);
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            String responseStr = IOUtils.toString(response.body().byteStream(), "UTF-8");
            DownloadFileInfoResponse downloadFileInfoResponse =
                JSONObject.toJavaObject(JSONObject.parseObject(responseStr), DownloadFileInfoResponse.class);
            if (DownloadFileInfoResponse.SUCCESS.equals(downloadFileInfoResponse.getStatus())) {
                return downloadFileInfoResponse.getBody();
            } else {
                return new DownloadFileInfo();
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new DownloadFileInfo();
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    private Request createOneRequest(String url) {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ak", ak);
        jsonObject.put("sk", sk);
        RequestBody requestBody = RequestBody.create(mediaType, jsonObject.toJSONString());
        Request request = new Request.Builder().post(requestBody)
            // 访问路径
            .url(url).build();
        return request;
    }

}
