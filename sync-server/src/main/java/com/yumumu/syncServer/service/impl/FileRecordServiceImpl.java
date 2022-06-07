package com.yumumu.syncServer.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yumumu.syncServer.mapper.FileRecordMapper;
import com.yumumu.syncServer.model.po.FileRecord;
import com.yumumu.syncServer.service.FileRecordService;

/**
 * @author zhl46
 * @description 针对表【FILE_RECORD(文件记录表)】的数据库操作Service实现
 * @createDate 2022-06-07 22:25:18
 */
@Service
public class FileRecordServiceImpl extends ServiceImpl<FileRecordMapper, FileRecord> implements FileRecordService {

    @Transactional
    @Override
    public void addNewFile(String name, String tempFileName, long size, String md5) {
        LambdaQueryWrapper<FileRecord> wrapper =
            Wrappers.<FileRecord>lambdaQuery().eq(FileRecord::getFilename, name).eq(FileRecord::getEnable, 1);
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
        } else {
            fileRecord = new FileRecord();
            fileRecord.setFileSize(size);
            fileRecord.setFilename(name);
            fileRecord.setEnable(1);
            fileRecord.setMd5(md5);
            fileRecord.setCreateTime(new Date());
            fileRecord.setTempName(tempFileName);
            fileRecord.setVersion(1);
        }

        save(fileRecord);
    }
}
