package com.lufax.jijin.fundation.repository;

import java.util.List;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.fundation.dto.JijinPromotionConfigDTO;
import com.lufax.mq.client.util.MapUtils;

import org.springframework.stereotype.Repository;

@Repository
public class JijinPromotionConfigRepository extends BaseRepository<JijinPromotionConfigDTO> {

    @Override
    protected String nameSpace() {
        return "BusJijinPromotionConfig";
    }

    public JijinPromotionConfigDTO findJijinPromotionConfigByFundCode(String fundCode) {
        return super.query("findJijinPromotionConfigByFundCode", MapUtils.buildKeyValueMap("fundCode", fundCode));
    }

    public int updateJijinPromotionConfig(JijinPromotionConfigDTO dto) {
        return update("updateJijinPromotionConfig", dto);
    }

    /**
     * 查询还在生效的促销（status=0）
     * @return
     */
    public List<JijinPromotionConfigDTO> findAllActivePromotionList(){
    	return super.queryList("findAllActivePromotionList",null);
    }
}
