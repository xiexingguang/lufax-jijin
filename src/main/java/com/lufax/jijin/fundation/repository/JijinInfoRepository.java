package com.lufax.jijin.fundation.repository;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.fundation.constant.FundType;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.mq.client.util.MapUtils;

@Repository
public class JijinInfoRepository extends BaseRepository<JijinInfoDTO> {

    @Override
    protected String nameSpace() {
        return "BusJijinInfo";
    }

    public JijinInfoDTO addJijinInfo(final JijinInfoDTO dto) {
        return this.insert("insertBusJijinInfo", dto);
    }

    public JijinInfoDTO findJijinInfo(Map condition) {
        return this.query("findJijinInfo", condition);
    }

    /**
     * 通过fundCode更新jijinInfo,不是ID
     * @param condition
     * @return
     */
    public int updateJijinInfo(Map condition) {
        return this.update("updateJijinInfo", condition);
    }

    public List<String> getDistinctInstId() {
        return this.queryListObject("getDistinctInstId", null);
    }
    

    public int updateJijinStatusByProductCode(Map condition){
    	return this.update("updateJijinStatusByProductCode", condition);
    }
    
    public List<JijinInfoDTO> findAllJijins(){
    	return this.queryList("findJijinInfo", null);

    }

    public JijinInfoDTO findJijinInfoByFundCode(String fundCode) {
        return this.query("findJijinInfoByFundCode", fundCode);
    }

    public JijinInfoDTO findJijinInfoByProductId(Long productId) {
        return this.query("findJijinInfoByProductId", productId);
    }
    
    /**
     * 查询全部直销基金
     * @return
     */
    public List<String> findZhixiaoFundCodeList(){
		return this.queryListObject("findZhixiaoFundCodeList",null);
    }
    
    public List<String> findFundCodeListByFundType(FundType fundType){
		return this.queryListObject("findFundCodeListByFundType",fundType.name());
    }
    
    public Map<String, ?> findByFundCodesAsOut(Collection<String> fundCodes, String fundType, String instId){
        return this.queryForMap("findByFundCodesAsOut", MapUtils.buildKeyValueMap("fundCodes", 
                new ArrayList<String>(new HashSet<String>(fundCodes)), "fundType", fundType, "instId", instId), "fundCode");
    }
}


