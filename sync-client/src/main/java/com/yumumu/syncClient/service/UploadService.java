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

import okhttp3.*;

/**
 * @author zhanghailin
 * @date 2022/6/7
 */
@Service
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
        // todo 请求获取token
        OkHttpClient okHttpClient = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ak", ak);
        jsonObject.put("sk", sk);
        jsonObject.put("fileName", fileName);
        jsonObject.put("fileMd5", md5);
        jsonObject.put("clientId", clientId);
        RequestBody requestBody = RequestBody.create(mediaType, jsonObject.toJSONString());
        Request request = new Request.Builder().post(requestBody).url("http://127.0.0.1:8088/upload/token").build();
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
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        response.close();
        System.out.println(result);
        return result;
    }

    private void upload(FileInfo fileInfo, String token) {
        // todo 上传文件
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), fileInfo.getFile());
        MultipartBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("file", fileInfo.getFileName(), fileBody).addFormDataPart("token", token)
            .addFormDataPart("clientId", clientId).build();

        Request request = new Request.Builder().post(requestBody).url(remoteUrl + "/upload/file").build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = null;
        try {
            result = IOUtils.toString(response.body().byteStream(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        response.close();
        System.out.println(result);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        executorService = Executors.newFixedThreadPool(maxThread);
    }
}
