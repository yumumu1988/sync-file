package com.yumumu.syncServer.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yumumu.syncServer.mapper.ClientsMapper;
import com.yumumu.syncServer.model.po.Clients;
import com.yumumu.syncServer.service.ClientsService;

/**
 * @author zhl46
 * @description 针对表【CLIENTS】的数据库操作Service实现
 * @createDate 2022-06-07 22:22:34
 */
@Service
public class ClientsServiceImpl extends ServiceImpl<ClientsMapper, Clients> implements ClientsService {

}
