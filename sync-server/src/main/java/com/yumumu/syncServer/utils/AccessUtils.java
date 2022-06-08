package com.yumumu.syncServer.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.yumumu.syncServer.model.bo.ClientInfo;

/**
 * @author zhanghailin
 * @date 2022/6/8
 */
@Component
public class AccessUtils {

    private static String ak;
    private static String sk;

    @Value("${access.ak}")
    private void setAk(String accessAk) {
        ak = accessAk;
    }

    @Value("${access.sk}")
    private void setSk(String accessSk) {
        sk = accessSk;
    }

    public static boolean access(String accessAk, String accessSk) {
        return ak.equals(accessAk) && sk.equals(accessSk);
    }

    public static boolean access(ClientInfo clientInfo) {
        return ak.equals(clientInfo.getAk()) && sk.equals(clientInfo.getSk());
    }
}
