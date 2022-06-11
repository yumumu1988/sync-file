package com.yumumu.syncServer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yumumu.syncServer.model.po.FileRecord;

/**
 * @author zhl46
 * @description 针对表【FILE_RECORD(文件记录表)】的数据库操作Service
 * @createDate 2022-06-07 22:25:18
 */
public interface FileRecordService extends IService<FileRecord> {

    void addNewFile(String name, String tempFileName, long size, String clientId, String md5);

    FileRecord getDownloadFileByClientIdAndIndex(String clientId, Integer fileIndex);

    FileRecord getFileRecordByTempName(String tempName);

    boolean isExisted(String fileMd5, String fileName);
}
