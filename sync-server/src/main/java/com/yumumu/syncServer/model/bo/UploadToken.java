package com.yumumu.syncServer.model.bo;

import java.io.Serializable;

import lombok.Data;

/**
 * @author zhanghailin
 * @date 2022/6/7
 */
@Data
public class UploadToken implements Serializable {
    private static final long serialVersionUID = -3928679224087105023L;

    private String token;
    private String fileName;
    private String fileMd5;
}
