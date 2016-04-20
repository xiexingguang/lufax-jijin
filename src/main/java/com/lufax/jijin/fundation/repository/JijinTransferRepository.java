package com.lufax.jijin.fundation.repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.fundation.dto.JijinTransferDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JijinTransferRepository extends BaseRepository<JijinTransferDTO> {

    protected String nameSpace() {
        return "JijinTransfer";
    }

    public Object insertJijinTransfer(JijinTransferDTO jijinTransferDTO) {
        return super.insert("insertJijinTransfer", jijinTransferDTO);
    }

    public void batchInsertJijinTransfer(List jijinTransferDTOs) {
        super.batchInsert("insertJijinTransfer", jijinTransferDTOs);
    }
}
