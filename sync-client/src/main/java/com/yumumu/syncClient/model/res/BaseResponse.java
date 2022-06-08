package com.yumumu.syncClient.model.res;

import java.io.Serializable;

import lombok.Data;

/**
 * @author zhanghailin
 * @date 2022/6/8
 */
@Data
public abstract class BaseResponse implements Serializable {
    private static final long serialVersionUID = -4726531196509921484L;

    public static final Integer SUCCESS = 1;
    public static final Integer FAILED = 0;

    private Integer status;
    private Integer errorCode;
    private String errorMsg;
}
