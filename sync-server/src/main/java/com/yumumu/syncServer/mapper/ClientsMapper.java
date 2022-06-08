package com.yumumu.syncServer.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yumumu.syncServer.model.po.Clients;

/**
 * @author zhl46
 * @description 针对表【CLIENTS】的数据库操作Mapper
 * @createDate 2022-06-07 22:22:34
 * @Entity .domain.com.yumumu.syncServer.model.po.Clients
 */
@Mapper
public interface ClientsMapper extends BaseMapper<Clients> {

    @Update("CREATE TABLE CLIENTS (ID INTEGER not null primary key auto_increment,CLIENT_ID VARCHAR (255) NOT NULL,CREATE_TIME TIMESTAMP NOT NULL,UPDATE_TIME TIMESTAMP NOT NULL,CURRENT_FILE_ID INTEGER NOT NULL,CLIENT_IP VARCHAR (255) NOT NULL); CREATE INDEX CLIENT_ID_INDEX ON CLIENTS (CLIENT_ID);")
    void initTable();
}
