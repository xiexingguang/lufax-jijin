package com.lufax.jijin.trade.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import com.lufax.jijin.dto.FundLoanRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.dao.FundLoanRequestDAO;
import com.lufax.jijin.dao.YlxProfitDAO;
import com.lufax.jijin.dto.FundLoanRequestDTO;
import com.lufax.jijin.ylx.batch.dto.YLXBatchDTO;
import com.lufax.jijin.ylx.batch.dto.YLXBatchFileDTO;
import com.lufax.jijin.ylx.batch.repository.YLXBatchFileRepository;
import com.lufax.jijin.ylx.batch.repository.YLXBatchRepository;
import com.lufax.jijin.ylx.dto.YlxFundInvestorProfitDTO;
import com.lufax.jijin.ylx.dto.YlxFundProdProfitDTO;
import com.lufax.jijin.ylx.dto.repository.YLXFundBalanceRepository;

@Service
@Scope
public class YLXProfitTransactionalService{
	@Autowired
    private YLXBatchRepository ylxBatchRepository;
    @Autowired
    private YLXBatchFileRepository ylxBatchFileRepository;
    @Autowired
    private YlxProfitDAO profitDao;
    @Autowired
    protected YLXFundBalanceRepository balanceDao;
    @Autowired
    private FundLoanRequestDAO fundLoanRequestDAO;
	
	@Transactional
    public void initBatchDB(YLXBatchDTO confirmBatch, List<YLXBatchFileDTO> confirmFiles){
		YLXBatchDTO batch = ylxBatchRepository.insertYLXBatchDTO(confirmBatch);
		for(YLXBatchFileDTO bf : confirmFiles){
			bf.setBatchId(batch.getId());
		}
        ylxBatchFileRepository.batchInsert(confirmFiles);
    }
	
	@Transactional
    public void investorProfitBatchInsert(List<YlxFundInvestorProfitDTO> batchList, long batchId, int lineNum){
		profitDao.batchInsert("insertYlxFundInvestorProfitHis", batchList);
		ylxBatchFileRepository.update("updateCurrentLineById", MapUtils.buildKeyValueMap("id", batchId, "currentLine", lineNum));
    }
	@Transactional
	public void productProfitBatchInsert(List<YlxFundProdProfitDTO> batchList, List<FundLoanRequestDTO> floans){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		for(YlxFundProdProfitDTO dto : batchList){
			for(FundLoanRequestDTO floan : floans){
				if(floan.getThirdCompanyCode().equals(dto.getProdCode())){
					try {
						balanceDao.updateYlxFundBalanceUnitPrice(floan.getProductCode(), dto.getUnitPrice(), format.parse(dto.getProfitDay()));
					} catch (ParseException e) {
						throw new RuntimeException("profitDay is not yyyyMMdd :" + dto.getProfitDay());
					}
					break;
				}
			}
		}
		profitDao.batchInsert("insertYlxFundProdProfitHis", batchList);
	}
}
