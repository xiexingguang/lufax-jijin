package com.lufax.jijin.base.repository;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.lufax.jijin.base.dto.BaseDTO;

import javax.annotation.Resource;

public abstract class TrddataBaseRepository<K extends BaseDTO> extends AbstractRepository<K> {

    @Resource(name = "sqlMapClient_trddata")
    protected SqlMapClient sqlMapClient;

    @Override
    protected SqlMapClient getSqlMapClient() {
        return sqlMapClient;
    }

}