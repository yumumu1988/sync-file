package com.yumumu.syncClient.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import okhttp3.*;

/**
 * @author zhanghailin
 * @date 2022/6/7
 */
@Service
public class DownloadService {

    @Value("${client.id}")

    public boolean download() {
        if (getFileId() > 0) {
            // todo 下载
            OkHttpClient okHttpClient = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ak", "1xxxx");
            jsonObject.put("sk", "z1233");
            RequestBody requestBody = RequestBody.create(mediaType, jsonObject.toJSONString());
            Request request = new Request.Builder().post(requestBody)
                // 访问路径
                .url("http://127.0.0.1:8088/download/client/xxx/file/1").build();
            Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();
                byte[] bytes = response.body().bytes();
                Files.write(Paths.get("D:\\z.txt"), bytes, StandardOpenOption.CREATE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 转化成byte数组

            return true;
        } else {
            return false;
        }
    }

    static boolean x = false;

    private Integer getFileId() {
        if (x) {
            return -1;
        } else {
            x = true;
            return 1;
        }
    }

}
