package com.yumumu.syncServer.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yumumu.syncServer.model.po.FileRecord;

/**
 * @author zhl46
 * @description 针对表【FILE_RECORD(文件记录表)】的数据库操作Mapper
 * @createDate 2022-06-07 22:25:18
 * @Entity .model.po.FileRecord
 */
@Mapper
public interface FileRecordMapper extends BaseMapper<FileRecord> {

}
