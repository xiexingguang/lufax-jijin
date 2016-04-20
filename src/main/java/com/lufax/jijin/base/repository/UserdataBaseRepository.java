package com.lufax.jijin.base.repository;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.lufax.jijin.base.dto.BaseDTO;

import javax.annotation.Resource;

public abstract class UserdataBaseRepository<K extends BaseDTO> extends AbstractRepository<K> {

    @Resource(name = "sqlMapClient_userdata")
    protected SqlMapClient sqlMapClient;

    @Override
    protected SqlMapClient getSqlMapClient() {
        return sqlMapClient;
    }

}