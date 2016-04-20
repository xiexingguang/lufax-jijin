/**
 *
 */
package com.lufax.jijin.fundation.repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.fundation.dto.JijinAccountDTO;
import com.lufax.mq.client.util.MapUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author chenguang E-mail:chenguang451@pingan.com.cn
 * @version create time:May 5, 2015 2:08:45 PM
 */
@Repository
public class JijinAccountRepository extends BaseRepository<JijinAccountDTO> {


    public JijinAccountDTO insertBusJijinAccount(JijinAccountDTO busJijinAccountDTO) {
        return super.insert("insertBusJijinAccount", busJijinAccountDTO);
    }

    public JijinAccountDTO findBusJijinAccount(Map condition) {
        return super.query("findBusJijinAccount", condition);
    }

    public JijinAccountDTO findActiveAccount(long userId, String instId, String channel) {
        return super.query("findBusJijinAccount", MapUtils.buildKeyValueMap(
                "userId", userId, "instId", instId, "channel", channel, "deleted", false));
    }
    
    public JijinAccountDTO findBusJijinAccountByPayNoAndInstId(String payNo, String instId) {
        return super.query("findBusJijinAccount", MapUtils.buildKeyValueMap(
                "payNo", payNo, "instId", instId));
    }
    

    List<JijinAccountDTO> findBusJijinAccountList(Map condition) {
        return super.queryList("findBusJijinAccount", condition);
    }

    public int updateBusJijinAccountById(Map condition) {
        return super.update("updateBusJijinAccountById", condition);
    }

    @Override
    protected String nameSpace() {
        return "BusJijinAccount";
    }

    public List<JijinAccountDTO> findValidAccountByUserId(Long userId) {
        return super.queryList("findValidAccountByUserId", userId);
    }

    public JijinAccountDTO findJijinAccountByInstIdAndContractNo(String instId,String contractNo){
    	return (JijinAccountDTO)super.queryObject("findJijinAccountByInstIdAndContractNo", MapUtils.buildKeyValueMap("instId",instId,"contractNo",contractNo));
    }

}
