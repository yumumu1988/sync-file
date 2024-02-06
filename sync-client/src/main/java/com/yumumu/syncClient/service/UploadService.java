package com.yumumu.syncClient.service;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.yumumu.syncClient.model.FileInfo;
import com.yumumu.syncClient.model.res.UploadTokenResponse;
import com.yumumu.syncClient.utils.HttpClientUtil;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

/**
 * @author zhanghailin
 * @date 2022/6/7
 */
@Service
@Slf4j
public class UploadService implements InitializingBean {

    @Value("${upload.ak}")
    private String ak;
    @Value("${upload.sk}")
    private String sk;
    @Value("${upload.max.thread}")
    private Integer maxThread;
    @Value("${remote.url}")
    private String remoteUrl;
    @Value("${client.id}")
    private String clientId;

    private ExecutorService executorService;

    public void uploadFile(FileInfo fileInfo) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                String uploadToken = getUploadToken(fileInfo.getFileName(), fileInfo.getFileMd5());
                if (!StringUtils.isEmpty(uploadToken)) {
                    upload(fileInfo, uploadToken);
                }
            }
        });
    }

    private String getUploadToken(String fileName, String md5) {
        // 请求获取token
        OkHttpClient okHttpClient = HttpClientUtil.getClient();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ak", ak);
        jsonObject.put("sk", sk);
        jsonObject.put("fileName", fileName);
        jsonObject.put("fileMd5", md5);
        jsonObject.put("clientId", clientId);
        RequestBody requestBody = RequestBody.create(mediaType, jsonObject.toJSONString());
        Request request = new Request.Builder().post(requestBody).url(remoteUrl + "/upload/token").build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = null;
        try {
            result = IOUtils.toString(response.body().byteStream(), "UTF-8");
            UploadTokenResponse uploadTokenResponse =
                JSONObject.toJavaObject(JSONObject.parseObject(result), UploadTokenResponse.class);
            if (UploadTokenResponse.SUCCESS.equals(uploadTokenResponse.getStatus())) {
                System.out.println("TOKEN " + uploadTokenResponse.getBody());
                return uploadTokenResponse.getBody();
            } else {
                log.error("get token failed. {}", uploadTokenResponse.getErrorMsg());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("get token failed. {}", e.getMessage());
            return null;
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    private void upload(FileInfo fileInfo, String token) {
        // 上传文件
        OkHttpClient okHttpClient = HttpClientUtil.getClient();
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), fileInfo.getFile());
        MultipartBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("file", fileInfo.getFileName(), fileBody).addFormDataPart("token", token)
                .addFormDataPart("relativePath", fileInfo.getRelativeFilePath()).addFormDataPart("clientId", clientId).build();

        Request request = new Request.Builder().post(requestBody).url(remoteUrl + "/upload/file").build();
        Response response = null;
        try {
            log.info("uploading file " + fileInfo.getFileName());
            response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = null;
        try {
            result = IOUtils.toString(response.body().byteStream(), "UTF-8");
            log.info(result);
            log.info(fileInfo.getFileName() + "has been uploaded");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        executorService = Executors.newFixedThreadPool(maxThread);
    }
}
