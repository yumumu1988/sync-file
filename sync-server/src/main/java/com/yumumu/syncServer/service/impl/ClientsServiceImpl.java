package com.yumumu.syncServer.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yumumu.syncServer.mapper.ClientsMapper;
import com.yumumu.syncServer.model.bo.DownloadFileInfo;
import com.yumumu.syncServer.model.po.Clients;
import com.yumumu.syncServer.model.po.FileRecord;
import com.yumumu.syncServer.service.ClientsService;

/**
 * @author zhl46
 * @description 针对表【CLIENTS】的数据库操作Service实现
 * @createDate 2022-06-07 22:22:34
 */
@Service
public class ClientsServiceImpl extends ServiceImpl<ClientsMapper, Clients> implements ClientsService {

    @Resource
    private FileRecordServiceImpl fileRecordService;

    @Transactional
    @Override
    public DownloadFileInfo getDownloadFileInfo(String clientId) {
        LambdaQueryWrapper<Clients> wrapper = Wrappers.<Clients>lambdaQuery();
        wrapper.eq(Clients::getClientId, clientId);
        Clients clients = this.baseMapper.selectOne(wrapper);
        Integer fileIndex = 0;
        if (null == clients) {
            // 初始化数据
            clients = new Clients();
            clients.setClientIp("");
            clients.setCreateTime(new Date());
            clients.setUpdateTime(new Date());
            clients.setClientId(clientId);
            clients.setCurrentFileId(fileIndex);
            save(clients);
        } else {
            fileIndex = clients.getCurrentFileId();
        }

        FileRecord fileRecord = fileRecordService.getDownloadFileByClientIdAndIndex(clientId, fileIndex);

        if (null == fileRecord) {
            return new DownloadFileInfo();
        }

        DownloadFileInfo downloadFileInfo = new DownloadFileInfo();
        downloadFileInfo.setFileName(fileRecord.getFilename());
        downloadFileInfo.setTempName(fileRecord.getTempName());
        return downloadFileInfo;
    }

    @Override
    public void updateFileIndex(String clientId, String tempName) {
        FileRecord fileRecord = fileRecordService.getFileRecordByTempName(tempName);
        if (null != fileRecord) {
            LambdaUpdateWrapper<Clients> wrapper = Wrappers.<Clients>lambdaUpdate();
            wrapper.eq(Clients::getClientId, clientId).set(Clients::getCurrentFileId, fileRecord.getId());
            this.baseMapper.update(null, wrapper);
        }
    }
}
