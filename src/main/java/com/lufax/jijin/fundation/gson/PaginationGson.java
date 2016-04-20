package com.lufax.jijin.fundation.gson;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.lufax.jijin.base.utils.Logger;

public class PaginationGson<T> extends BaseResponseGson {
    private int pageLimit;
    private int totalCount;              //recordCount
    private int totalPage;               //pageCount
    private int currentPage;             //curPageNo
    protected int prePage;               //prePageNo
    protected int nextPage;              //nextPageNo
    private List<T> data;                //pageRecords
    private String retMsg;

    protected Map<String, Object> attachedInfo;

    @SuppressWarnings("unchecked")
    public PaginationGson(int pageLimit, int recordCount,
                          int curPageNo, List<T> pageRecords, String retMsg, String retCode) {

        this.setRetMessage(retMsg);
        this.setRetCode(retCode);
        this.pageLimit = pageLimit;
        this.totalCount = recordCount;
//		this.totalPage = (recordCount<=0) ? 0 : ((0==recordCount%pageLimit) ? recordCount/pageLimit : (recordCount/pageLimit)+1);
        this.totalPage = (int)Math.ceil((double)recordCount / (double)pageLimit);
//		this.currentPage = this.caculateCurrentPage(this.totalPage, curPageIndex);
        this.currentPage = curPageNo;
        //若当前页已是第一页，那么前一页就是当前页；否则前一页就是当前页-1
        this.prePage = curPageNo<=1 ? 1 : curPageNo-1;
//		this.prePage = currentIndex<=0 ? 0 : currentIndex-1;
        //若当前页已是最后一页，那么下一页就是当前页；否则下一页就是当前页+1
        this.nextPage = curPageNo>=totalPage ? curPageNo : curPageNo+1;
//		this.nextPage = curPageIndex+1>=totalPage ? curPageIndex : curPageIndex+1;
        this.data = (null==pageRecords ? Collections.EMPTY_LIST : pageRecords);
        this.retMsg = retMsg;
    }

    public PaginationGson(int pageLimit, int recordCount, int curPageNo, List<T> pageRecords,
                          String retMsg, String retCode, Map<String, Object> attachedInfo) {
        this(pageLimit, recordCount, curPageNo, pageRecords, retMsg, retCode);
        this.attachedInfo = attachedInfo;
    }



    public int getPageLimit() {
        return pageLimit;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPrePage() {
        return prePage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public List<T> getData() {
        return data;
    }

    public String getRetMsg() {
        return retMsg;
    }
//	private int caculateCurrentPage(int totalPage, int requestPage) {
//		int smallestPage = 1;
//		try {
//			return Math.min(totalPage, Math.max(smallestPage, requestPage));
//		} catch (Exception e) {
//			Logger.error(this, String.format("Invalid page num (%d)", requestPage), e);
//			return smallestPage;
//		}
//	}

    public Map<String, Object> getAttachedInfo() {
        return attachedInfo;
    }
}
