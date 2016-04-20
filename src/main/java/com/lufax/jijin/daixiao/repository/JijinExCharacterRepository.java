package com.lufax.jijin.daixiao.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.daixiao.dto.JijinExCharacterDTO;
import com.lufax.jijin.fundation.schedular.SyncFileRepository;
import com.lufax.mq.client.util.MapUtils;

/**
 * Created by chenguang451 on 2016/1/5.
 */
@Repository
public class JijinExCharacterRepository extends BaseRepository<JijinExCharacterDTO> implements SyncFileRepository {

    @Override
    protected String nameSpace() {
        return "BusJijinExCharacter";
    }

    public JijinExCharacterDTO insertJijinExCharacter(JijinExCharacterDTO dto) {
        return super.insert("insert", dto);
    }

    public void batchInsertJijinExCharacter(List<JijinExCharacterDTO> list){
        batchInsert("insert",list);
    }

    public JijinExCharacterDTO queryLatestRecordByFundCode(String fundCode){
        return super.query("selectLatestRecordByFundCode", MapUtils.buildKeyValueMap("fundCode", fundCode));
    }

    @Override
    public int batchInsertDTOs(List dtos) {
        return batchInsert("insert",dtos);
    }

    /**
     * 是否支持货转股转购出
     * @param fundCodes
     * @return
     */
    public List<String> queryMtsRedeemFundCodes(Collection<String> fundCodes){
        return (List<String>)super.queryListObject("selectMtsRedeem", 
                MapUtils.buildKeyValueMap("fundCodes", new ArrayList<String>(new HashSet<String>(fundCodes)), "isMtsRedeem", 1));
    }
    
    /**
     * 是否支持货转股转购入
     * @param fundCodes
     * @return
     */
    public List<String> queryMtsSubFundCodes(Collection<String> fundCodes){
        return (List<String>)super.queryListObject("selectMtsSub", 
                MapUtils.buildKeyValueMap("fundCodes", new ArrayList<String>(new HashSet<String>(fundCodes)), "isMtsSub", 1));
    }
}
