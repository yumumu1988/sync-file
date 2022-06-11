package com.yumumu.syncServer.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    private static String cookieDomain;

    public static void addCookie(String username, String password, HttpServletResponse httpServletResponse) {
        long currentTimeMillis = System.currentTimeMillis() + 3600 * 1000;
        String token = TokenUtils.getToken(username, password, String.valueOf(currentTimeMillis));
        Cookie cookie = new Cookie("token", token);
        cookie.setDomain(cookieDomain);
        cookie.setPath("/");
        Cookie timestampCookie = new Cookie("timestamp", String.valueOf(currentTimeMillis));
        timestampCookie.setDomain(cookieDomain);
        timestampCookie.setPath("/");
        httpServletResponse.addCookie(cookie);
        httpServletResponse.addCookie(timestampCookie);
    }

    @Value("${cookie.domain}")
    private void setCookieDomain(String domain) {
        cookieDomain = domain;
    }

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

    public static boolean access(HttpServletRequest httpServletRequest) {
        String token = null;
        String timestamp = null;
        if (httpServletRequest.getCookies() == null) {
            return false;
        }
        for (Cookie cookie : httpServletRequest.getCookies()) {
            if ("token".equals(cookie.getName())) {
                token = cookie.getValue();
            }
            if ("timestamp".equals(cookie.getName())) {
                timestamp = cookie.getValue();
            }
        }
        if (token == null || timestamp == null) {
            return false;
        }
        try {
            if (Long.parseLong(timestamp) < System.currentTimeMillis()) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return TokenUtils.getToken(ak, sk, timestamp).equals(token);
    }
}
