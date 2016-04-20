package com.lufax.jijin.dao;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.lufax.jersey.utils.Logger;

import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public abstract class BaseDAO {

    private SqlMapClientTemplate sqlMapClientTemplate;

    protected SqlMapClientTemplate getSqlMapClientTemplate() {
        return this.sqlMapClientTemplate;
    }

    public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
        this.sqlMapClientTemplate = sqlMapClientTemplate;
    }

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClientTemplate = new SqlMapClientTemplate(sqlMapClient);
    }

    protected <T> T insert(String statement, Object param) {
        return (T) this.getSqlMapClientTemplate().insert(generate(statement), param);
    }

    private String generate(String statement) {
        return nameSpace() + "." + statement;
    }

    protected int update(String statement, Object param) {
        return this.getSqlMapClientTemplate().update(generate(statement), param);
    }

    protected <T> T queryForObject(String statement, Object param) {
        return (T) this.getSqlMapClientTemplate().queryForObject(generate(statement), param);
    }
    
/*    protected Object selectAObject(String statement, Object param) {
        return this.getSqlMapClientTemplate().queryForObject(generate(statement), param);
    }*/
    protected Map queryForMap(String statement, Object param, String keyProperty) {
        return this.getSqlMapClientTemplate().queryForMap(generate(statement), param, keyProperty);
    }

    @SuppressWarnings("unchecked")
    protected <T> List<T> queryForList(String statement, Object param) {
        List<T> result = this.getSqlMapClientTemplate().queryForList(generate(statement), param);
        return result;
    }
    @Transactional
    public int batchInsert(final String sqlId, final List dList) {
        try {
        	sqlMapClientTemplate.getSqlMapClient().startBatch();
            for (Object t : dList) {
                insert(sqlId, t);
            }
            return sqlMapClientTemplate.getSqlMapClient().executeBatch();
        } catch (SQLException e) {
            Logger.error(this, String.format("REPOSITORY batch insert failed, sqlId=%s ,domain=%s", sqlId, dList), e);
            return 0;
        }
    }
    @Transactional
    public int batchUpdate(final String sqlId, final List dList) {
        try {
        	sqlMapClientTemplate.getSqlMapClient().startBatch();
            for (Object t : dList) {
                update(sqlId, t);
            }
            return sqlMapClientTemplate.getSqlMapClient().executeBatch();
        } catch (SQLException e) {
            Logger.error(this, String.format("REPOSITORY batch insert failed, sqlId=%s ,domain=%s", sqlId, dList), e);
            return 0;
        }
    }
    protected abstract String nameSpace();
}
