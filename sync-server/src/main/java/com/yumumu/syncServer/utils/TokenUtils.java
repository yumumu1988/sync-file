package com.yumumu.syncServer.utils;

import java.util.UUID;

/**
 * @author zhanghailin
 * @date 2022/6/7
 */
public class TokenUtils {

    public static boolean checkToken(String md5, String fileName, String token) {
        return true;
    }

    public static String getToken(String md5, String fileName) {
        return UUID.randomUUID().toString();
    }

}
