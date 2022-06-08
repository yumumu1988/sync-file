package com.yumumu.syncClient.model.res;

import java.io.Serializable;

import com.yumumu.syncClient.model.DownloadFileInfo;

import lombok.Data;

/**
 * @author zhanghailin
 * @date 2022/6/8
 */
@Data
public class DownloadFileInfoResponse extends BaseResponse implements Serializable {
    private static final long serialVersionUID = -8138056560280136451L;

    private DownloadFileInfo body;
}
