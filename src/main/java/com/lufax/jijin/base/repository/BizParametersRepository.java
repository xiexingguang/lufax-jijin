package com.lufax.jijin.base.repository;

import com.lufax.jijin.base.dto.BizParametersDTO;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import org.springframework.stereotype.Repository;

@Repository
public class BizParametersRepository extends CfgdataBaseRepository<BizParametersDTO> {

    @Override
    protected String nameSpace() {
        return "BIZ_PARAMETERS";
    }

    public String findValueByCode(String parameterCode) {
        BizParametersDTO result = findByCode(parameterCode);
        if (result == null) {
            Logger.warn(this, String.format("The value of %s has not been found in biz parameter.", parameterCode));
            return null;
        }
        return result.getValue();
    }

    public BizParametersDTO findByCode(String parameterCode) {
        return query("findByCode", MapUtils.buildKeyValueMap("parameterCode", parameterCode));
    }
    
    public int updateByCode(String parameterCode, String value) {
        return update("updateValueByCode", MapUtils.buildKeyValueMap("code", parameterCode,"value", value));
    }
    
}
