package com.yumumu.syncServer.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

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

    @Update("CREATE TABLE FILE_RECORD (ID INTEGER not null primary key auto_increment,FILENAME VARCHAR (255) NOT NULL,FILE_SIZE BIGINT NOT NULL,MD5 VARCHAR (255) NOT NULL,VERSION INTEGER NOT NULL,CREATE_TIME TIMESTAMP NOT NULL,ENABLE TINYINT NOT NULL,TEMP_NAME VARCHAR (255) NOT NULL,CLIENT_ID VARCHAR(255) not NULL); COMMENT ON TABLE FILE_RECORD IS '文件记录表';create index FILENAME_INDEX  on FILE_RECORD (FILENAME);create index TEMPNAME_INDEX  on FILE_RECORD (TEMP_NAME);")
    void initTable();

}
