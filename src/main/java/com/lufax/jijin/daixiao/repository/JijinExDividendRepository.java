package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.daixiao.dto.JijinExDividendDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * 历史分红信息
 * @author 
 *
 */
@Repository
public class JijinExDividendRepository extends BaseRepository<JijinExDividendDTO> {

    @Override
    protected String nameSpace() {
        return "BusJijinExDividend";
    }

    public JijinExDividendDTO insertJijinExDividend(JijinExDividendDTO jijinExDividendDTO) {
        return super.insert("insertJijinExDividend", jijinExDividendDTO);
    }

    public List<JijinExDividendDTO> getJijinExDividendByFundCode(String fundCode) {
        return super.queryList("getJijinExDividendByFundCode", fundCode);
    }

    public void batchInsertDTOs(List dtos) {
        super.batchInsert("insertJijinExDividend", dtos);
    }
}
