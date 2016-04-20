package com.lufax.jijin.fundation.gson;

import java.util.List;
import java.util.Map;

public class PaginationByPageIndexGson<T> extends PaginationGson<T> {

    public PaginationByPageIndexGson(int pageLimit, int recordCount, int curPageIndex,
            List<T> pageRecords, String retMsg, String retCode) {
        
        super(pageLimit, recordCount, curPageIndex, pageRecords, retMsg, retCode);
        //写0而不写curPageIndex，防止curPageIndex为负值
        this.prePage = curPageIndex<=0 ? 0 : curPageIndex-1;
        //写curPageIndex而不写super.getTotalPage()-1，防止super.getTotalPage()-1为负值
        this.nextPage = (curPageIndex+1>=super.getTotalPage()) ? curPageIndex : curPageIndex+1;
    }

    public PaginationByPageIndexGson(int pageLimit, int recordCount, int curPageIndex,
            List<T> pageRecords, String retMsg, String retCode, Map<String, Object> attachedInfo) {
        this(pageLimit, recordCount, curPageIndex, pageRecords, retMsg, retCode);
        this.attachedInfo = attachedInfo;
    }
}
