package com.yumumu.syncServer.service.impl;

import java.util.Date;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yumumu.syncServer.mapper.FileRecordMapper;
import com.yumumu.syncServer.model.po.FileRecord;
import com.yumumu.syncServer.service.FileRecordService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhl46
 * @description 针对表【FILE_RECORD(文件记录表)】的数据库操作Service实现
 * @createDate 2022-06-07 22:25:18
 */
@Service
@Slf4j
public class FileRecordServiceImpl extends ServiceImpl<FileRecordMapper, FileRecord>
    implements FileRecordService, InitializingBean {

    @Transactional
    @Override
    public void addNewFile(String name, String tempFileName, long size, String clientId, String md5) {
        LambdaQueryWrapper<FileRecord> wrapper = Wrappers.<FileRecord>lambdaQuery().eq(FileRecord::getFilename, name)
            .eq(FileRecord::getEnable, 1).ne(FileRecord::getClientId, clientId);
        FileRecord fileRecord = this.baseMapper.selectOne(wrapper);

        if (null != fileRecord) {
            fileRecord.setEnable(0);
            updateById(fileRecord);

            fileRecord.setId(null);
            fileRecord.setVersion(fileRecord.getVersion() + 1);
            fileRecord.setMd5(md5);
            fileRecord.setCreateTime(new Date());
            fileRecord.setEnable(1);
            fileRecord.setTempName(tempFileName);
            fileRecord.setFileSize(size);
            fileRecord.setClientId(clientId);
        } else {
            fileRecord = new FileRecord();
            fileRecord.setFileSize(size);
            fileRecord.setFilename(name);
            fileRecord.setEnable(1);
            fileRecord.setMd5(md5);
            fileRecord.setCreateTime(new Date());
            fileRecord.setTempName(tempFileName);
            fileRecord.setVersion(1);
            fileRecord.setClientId(clientId);
        }

        save(fileRecord);
    }

    @Override
    public FileRecord getDownloadFileByClientIdAndIndex(String clientId, Integer fileIndex) {
        LambdaQueryWrapper<FileRecord> wrapper = Wrappers.<FileRecord>lambdaQuery();
        wrapper.eq(FileRecord::getEnable, 1).ne(FileRecord::getClientId, clientId).gt(FileRecord::getId, fileIndex)
            .orderByAsc(FileRecord::getId);
        wrapper.last("limit 1");
        return this.baseMapper.selectOne(wrapper);
    }

    @Override
    public FileRecord getFileRecordByTempName(String tempName) {
        LambdaQueryWrapper<FileRecord> wrapper = Wrappers.<FileRecord>lambdaQuery();
        wrapper.eq(FileRecord::getTempName, tempName);
        return this.baseMapper.selectOne(wrapper);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            log.info("CHECK FILE_RECORD");
            this.baseMapper.selectById(0);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e.getMessage().contains("Table \"FILE_RECORD\" not found")) {
                this.baseMapper.initTable();
                log.info("CREATE FILE_RECORD");
            }
        }
    }
}
