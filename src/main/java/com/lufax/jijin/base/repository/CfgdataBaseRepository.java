package com.lufax.jijin.base.repository;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.lufax.jijin.base.dto.BaseDTO;

import javax.annotation.Resource;

public abstract class CfgdataBaseRepository<K extends BaseDTO> extends AbstractRepository<K> {

    @Resource(name = "sqlMapClient_cfgdata")
    protected SqlMapClient sqlMapClient;

    @Override
    protected SqlMapClient getSqlMapClient() {
        return sqlMapClient;
    }

}