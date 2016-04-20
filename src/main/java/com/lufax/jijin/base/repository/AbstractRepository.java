package com.lufax.jijin.base.repository;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.lufax.jijin.base.dto.BaseDTO;
import com.lufax.jijin.base.utils.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public abstract class AbstractRepository<T extends BaseDTO> {

    protected abstract SqlMapClient getSqlMapClient();

    protected int PAGE_COUNT = 100;

    public T insert(String sqlId, T dto) {
        try {
            dto.setId((Long) getSqlMapClient().insert(getSqlId(sqlId), dto));
            return dto;
        } catch (SQLException e) {
        	Logger.error(this, "insert error",e);
            throw new RuntimeException(String.format("REPOSITORY insert failed, sqlId=%s ,dto=%s.", sqlId, dto), e);
        }
    }

    public int batchInsert(final String sqlId, final List<T> dList) {
        try {
            getSqlMapClient().startBatch();
            for (T t : dList) {
                insert(sqlId, t);
            }
            return getSqlMapClient().executeBatch();
        } catch (SQLException e) {
            Logger.error(this, String.format("REPOSITORY batch insert failed, sqlId=%s ,domain=%s", sqlId, dList), e);
            return 0;
        }
    }

    public int batchUpdate(final String sqlId, final List dList) {
        try {
            getSqlMapClient().startBatch();
            for (Object t : dList) {
                update(sqlId, t);
            }
            return getSqlMapClient().executeBatch();
        } catch (SQLException e) {
            Logger.error(this, String.format("REPOSITORY batch insert failed, sqlId=%s ,domain=%s", sqlId, dList), e);
            return 0;
        }
    }
    public int batchUpdateMap(final String sqlId, final List<Map> dList) {
        try {
            getSqlMapClient().startBatch();
            for (Map t : dList) {
                update(sqlId, t);
            }
            return getSqlMapClient().executeBatch();
        } catch (SQLException e) {
            Logger.error(this, String.format("REPOSITORY batch update failed, sqlId=%s ,domain=%s", sqlId, dList), e);
            return 0;
        }
    }

    public int update(String sqlId, Object dto) {
        try {
            return getSqlMapClient().update(getSqlId(sqlId), dto);
        } catch (SQLException e) {
            throw new RuntimeException(String.format("REPOSITORY update failed, sqlId=%s ,dto=%s.", sqlId, dto), e);
        }
    }

    public Integer update(String sqlId, Map map) {
        try {
            return getSqlMapClient().update(getSqlId(sqlId), map);
        } catch (SQLException e) {
            throw new RuntimeException(String.format("REPOSITORY update failed, sqlId=%s ,map=%s.", sqlId, map), e);
        }
    }

    public T query(String sqlId, Object param) {
        try {
            return (T) getSqlMapClient().queryForObject(getSqlId(sqlId), param);
        } catch (SQLException e) {
            throw new RuntimeException(String.format("REPOSITORY query failed, sqlId=%s ,param=%s.", sqlId, param), e);
        }
    }

    public Object queryObject(String sqlId, Object param) {
        try {
            return getSqlMapClient().queryForObject(getSqlId(sqlId), param);
        } catch (SQLException e) {
            throw new RuntimeException(String.format("REPOSITORY query failed, sqlId=%s ,param=%s.", sqlId, param), e);
        }
    }

    public Object queryObject(String sqlId) {
        try {
            return getSqlMapClient().queryForObject(getSqlId(sqlId));
        } catch (SQLException e) {
            throw new RuntimeException(String.format("REPOSITORY query failed, sqlId=%s.", sqlId), e);
        }
    }

    public List queryListObject(String sqlId, Object param) {
        try {
            return getSqlMapClient().queryForList(getSqlId(sqlId), param);
        } catch (SQLException e) {
            throw new RuntimeException(String.format("REPOSITORY queryList failed, sqlId=%s ,param=%s.", sqlId, param), e);
        }
    }


    public List<T> queryList(String sqlId, Object param) {
        try {
            return getSqlMapClient().queryForList(getSqlId(sqlId), param);
        } catch (SQLException e) {
            throw new RuntimeException(String.format("REPOSITORY queryList failed, sqlId=%s ,param=%s.", sqlId, param), e);
        }
    }


    //pageIndex 从0开始
    //countPerPage 每页size
    public List<T> selectForListPagination(String sqlId, Object params, int pageIndex, int countPerPage) {

        //若入参pageIndex<0,就把pageIndex设为0，否则pageIndex就等于入参值
        if (pageIndex < 0) {
            pageIndex = 0;
        } 
        if (countPerPage < 0) {
            countPerPage = PAGE_COUNT;
        }
        int skip = pageIndex * countPerPage;
        try {
            return getSqlMapClient().queryForList(getSqlId(sqlId), params, skip, countPerPage);
        } catch (SQLException e) {
            throw new RuntimeException(String.format("selectForListPagination query failed, sqlId=%s, param=%s.", getSqlId(sqlId), params), e);
        }
    }

    public List<T> queryList(String sqlId, Object param, int skip, int max) {
        try {
            return getSqlMapClient().queryForList(getSqlId(sqlId), param, skip, max);
        } catch (SQLException e) {
            throw new RuntimeException(String.format("REPOSITORY queryList failed, sqlId=%s ,param=%s.", sqlId, param), e);
        }
    }

    private String getSqlId(String sqlId) {
        return String.format("%s.%s", nameSpace(), sqlId);
    }
    
    public Map<String, ?> queryForMap(String sqlId, Object param, String keyStr) {
    	try {
			return getSqlMapClient().queryForMap(getSqlId(sqlId), param, keyStr);
		} catch (SQLException e) {
			throw new RuntimeException(String.format("REPOSITORY queryMap failed, sqlId=%s ,param=%s.", sqlId, param), e);
		}
    }

    protected abstract String nameSpace();
}
