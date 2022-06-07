package com.yumumu.syncServer.mapper;

import org.apache.ibatis.annotations.Mapper;

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

}
