package com.yumumu.syncClient.model.res;

import java.io.Serializable;

import lombok.Data;

/**
 * @author zhanghailin
 * @date 2022/6/8
 */
@Data
public class UploadTokenResponse extends BaseResponse implements Serializable {
    private static final long serialVersionUID = -7743840954369637736L;

    private String body;
}
