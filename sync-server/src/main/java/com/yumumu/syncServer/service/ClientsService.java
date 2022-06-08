package com.yumumu.syncServer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yumumu.syncServer.model.bo.DownloadFileInfo;
import com.yumumu.syncServer.model.po.Clients;

/**
 * @author zhl46
 * @description 针对表【CLIENTS】的数据库操作Service
 * @createDate 2022-06-07 22:22:34
 */
public interface ClientsService extends IService<Clients> {

    DownloadFileInfo getDownloadFileInfo(String clientId);

    void updateFileIndex(String clientId, String tempName);
}
