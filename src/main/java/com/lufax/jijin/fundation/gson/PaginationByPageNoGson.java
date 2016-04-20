package com.lufax.jijin.fundation.gson;

import java.util.List;
import java.util.Map;

public class PaginationByPageNoGson<T> extends PaginationGson<T> {


    public PaginationByPageNoGson(int pageLimit, int recordCount, int curPageNo, 
            List<T> pageRecords, String retMsg, String retCode) {
        super(pageLimit, recordCount, curPageNo, pageRecords, retMsg, retCode);
    }
	
    public PaginationByPageNoGson(int pageLimit, int recordCount, int curPageNo,
            List<T> pageRecords, String retMsg, String retCode, Map<String, Object> attachedInfo) {
        super(pageLimit, recordCount, curPageNo, pageRecords, retMsg, retCode, attachedInfo);
    }


}
