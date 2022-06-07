package com.yumumu.syncServer.model.bo;

import java.io.Serializable;

import lombok.Data;

/**
 * @author zhanghailin
 * @date 2022/6/7
 */
@Data
public class ClientInfo implements Serializable {
    private static final long serialVersionUID = -2777394179721190040L;

    private String ak;
    private String sk;
    private String fileName;
    private String fileMd5;
}
