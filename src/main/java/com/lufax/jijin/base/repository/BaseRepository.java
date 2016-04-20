package com.lufax.jijin.base.repository;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.lufax.jijin.base.dto.BaseDTO;

import javax.annotation.Resource;

/**
 * 
 * this is POF_OPR schema
 * @author xuneng
 *
 * @param <K>
 */
public abstract class BaseRepository<K extends BaseDTO> extends AbstractRepository<K> {

    @Resource(name = "sqlMapClient")
    protected SqlMapClient sqlMapClient;

    @Override
    protected SqlMapClient getSqlMapClient() {
        return sqlMapClient;
    }

}
