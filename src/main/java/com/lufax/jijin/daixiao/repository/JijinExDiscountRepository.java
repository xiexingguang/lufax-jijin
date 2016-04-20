package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.dto.JijinExDiscountDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 费率折扣
 *
 * @author chenqunhui
 */
@Repository
public class JijinExDiscountRepository extends BaseRepository<JijinExDiscountDTO> {

    @Override
    protected String nameSpace() {
        return "BusJijinExDiscount";
    }

    public JijinExDiscountDTO insertJijinExDiscount(JijinExDiscountDTO JijinExDiscountDTO) {
        return super.insert("insertJijinExDiscount", JijinExDiscountDTO);
    }

    public void batchInsertJijinExDiscount(List<JijinExDiscountDTO> list) {
        batchInsert("insertJijinExDiscount", list);
    }

    public int updateJijinExDiscount(Map condition) {
        return super.update("updateJijinExDiscount", condition);
    }

    public List<JijinExDiscountDTO> getJijinExDiscount(Map condition) {
        return super.queryList("getJijinExDiscount", condition);
    }

    public JijinExDiscountDTO getJijinExDiscountById(Long id) {
        List<JijinExDiscountDTO> jijinExDiscountDTOs = super.queryList("getJijinExDiscount", MapUtils.buildKeyValueMap("id", id));
        if (jijinExDiscountDTOs != null && jijinExDiscountDTOs.size() > 0) {
            return jijinExDiscountDTOs.get(0);
        } else {
            return null;
        }
    }

    public List<JijinExDiscountDTO> getJijinExDiscountByFundCodeAndBizCode(String fundCode, String bizCode) {
        return super.queryList("getJijinExDiscountByFundCodeAndBizCode", MapUtils.buildKeyValueMap("fundCode", fundCode, "bizCode", bizCode));
    }

    public Long getLatestBatchIdByFundCodeAndType(String fundCode, String bizCode) {
        return (Long) super.queryObject("getLatestBatchIdByFundCodeAndType", MapUtils.buildKeyValueMap("fundCode", fundCode, "bizCode", bizCode));
    }

    public Long getLatestBatchIdByDate(String fundCode, String bizCode, String currentDate) {
        return (Long) super.queryObject("getLatestBatchIdByDate", MapUtils.buildKeyValueMap("fundCode", fundCode, "bizCode", bizCode, "currentDate", currentDate));
    }

    public List<JijinExDiscountDTO> getJijinExDiscountsByBatchIdFundCodeAndType(Long batchId,String fundCode, String bizCode) {
        return super.queryList("getJijinExDiscountsByBatchIdFundCodeAndType",  MapUtils.buildKeyValueMap("batchId",batchId,"fundCode", fundCode, "bizCode", bizCode));
    }

    public List<JijinExDiscountDTO> getUnDispachedJijinExDiscount(int limit) {
        return super.queryList("getJijinExDiscount", MapUtils.buildKeyValueMap("limit", limit, "status", "NEW"));
    }
}
