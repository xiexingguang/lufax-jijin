package com.lufax.jijin.ylx.dto.repository;

import org.springframework.stereotype.Repository;

import com.lufax.jijin.base.repository.BusdataBaseRepository;
import com.lufax.jijin.ylx.dto.YLXTransactionHistoryDTO;
@Repository
public class YLXTransactionHistoryRepository extends BusdataBaseRepository<YLXTransactionHistoryDTO> {

	@Override
	protected String nameSpace() {
		return "ylxOpTransactionHistory";
	}
    public Object insertYlxOpTransactionHistory(YLXTransactionHistoryDTO ylxTransactionHistoryDTO) {
        return super.insert("insertYlxOpTransactionHistory", ylxTransactionHistoryDTO);
    }
}
