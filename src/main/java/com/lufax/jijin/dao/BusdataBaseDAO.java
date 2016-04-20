package com.lufax.jijin.dao;

import com.ibatis.sqlmap.client.SqlMapClient;

import javax.annotation.Resource;

public abstract class BusdataBaseDAO extends BaseDAO{

    @Resource(name = "sqlMapClient_busdata")
    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        super.setSqlMapClient(sqlMapClient);
    }
}
