package com.yumumu.syncClient.utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * @author zhanghailin
 * @date 2022/6/7
 */
public class HttpClientUtil {

    static OkHttpClient OK_HTTP_CLIENT = null;

    static {
        OK_HTTP_CLIENT = new OkHttpClient.Builder().writeTimeout(10, TimeUnit.MINUTES)
            .connectTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.MINUTES).build();
    }

    public static OkHttpClient getClient() {
        return OK_HTTP_CLIENT;
    }

}
