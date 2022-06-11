package com.yumumu.syncServer.model.bo;

import java.io.Serializable;

import lombok.Data;

/**
 * @author zhanghailin
 * @date 2022/6/11
 */
@Data
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 4710023344495096104L;

    private String username;
    private String password;
}
