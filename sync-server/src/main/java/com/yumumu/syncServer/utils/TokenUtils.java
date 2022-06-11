package com.yumumu.syncServer.utils;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

/**
 * @author zhanghailin
 * @date 2022/6/7
 */
@Component
public class TokenUtils {

    private static String salt;

    @Value("${token.salt}")
    private void setSalt(String tokenSalt) {
        salt = tokenSalt;
    }

    public static boolean checkToken(String md5, String fileName, String clientId, String token) {
        System.out.println("CHECK TOKEN: " + md5 + " " + fileName + " " + clientId);
        return generateToken(md5, fileName, clientId).equals(token);
    }

    public static String getToken(String md5, String fileName, String clientId) {
        System.out.println("GET TOKEN: " + md5 + " " + fileName + " " + clientId);
        return generateToken(md5, fileName, clientId);
    }

    private static String generateToken(String md5, String fileName, String clientId) {
        String content = String.format("%s %s %s", md5, fileName, clientId);
        return generate(content);
    }

    @NotNull
    private static String generate(String content) {
        System.out.println("CONTENT " + content);
        for (int i = 0; i < (Math.min(salt.length(), 10)); i++) {
            char ch = salt.charAt(i);
            content = content.replaceAll(Integer.toString(i), Character.toString(ch));
        }
        return DigestUtils.md5DigestAsHex(content.getBytes());
    }

}
