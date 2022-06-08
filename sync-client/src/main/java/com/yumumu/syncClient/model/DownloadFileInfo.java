package com.yumumu.syncClient.model;

import java.io.Serializable;

import lombok.Data;

/**
 * @author zhanghailin
 * @date 2022/6/8
 */
@Data
public class DownloadFileInfo implements Serializable {
    private static final long serialVersionUID = -9147482660855612242L;

    private String fileName;
    private String tempName;
}
