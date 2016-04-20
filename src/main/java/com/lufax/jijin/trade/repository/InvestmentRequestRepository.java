package com.lufax.jijin.trade.repository;

import com.lufax.jijin.base.repository.TrddataBaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.trade.dto.InvestmentRequestDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InvestmentRequestRepository extends TrddataBaseRepository<InvestmentRequestDTO> {

    private static final String NAME_SPACE = "INVESTMENT_REQUEST";
    private static final String GET_INVESTMENT_REQUEST_BY_TRX_ID = "getInvestmentRequestByTrxId";
    private static final String GET_INVESTMENT_REQUEST_BY_PRODUCT_ID = "getInvestmentRequestByProductId";
    private static final String GET_INVESTMENT_REQUEST_BY_ID = "getInvestmentRequestById";
    private static final String GET_INVESTMENT_REQUEST_BT_TRX_ID_IGNORING_STATUS = "getInvestmentRequestByTrxIdIgnoringStatus";

    @Override
    protected String nameSpace() {
        return NAME_SPACE;
    }

    public InvestmentRequestDTO getInvestmentRequestById(long id) {
        return query(GET_INVESTMENT_REQUEST_BY_ID, id);
    }

    public InvestmentRequestDTO getInvestmentRequestByTrxId(long trxId) {
        return query(GET_INVESTMENT_REQUEST_BY_TRX_ID, trxId);
    }

    public List<InvestmentRequestDTO> getInvestmentRequestByProductId(Long productId) {
        return queryList(GET_INVESTMENT_REQUEST_BY_PRODUCT_ID, productId);
    }

    public InvestmentRequestDTO getByTrxIdIgnoringStatus(long trxId) {
        return query(GET_INVESTMENT_REQUEST_BT_TRX_ID_IGNORING_STATUS, trxId);
    }

    public List<InvestmentRequestDTO> getInvestmentRequestDTOsByIds(List<Long> ids) {
        return queryList("getInvestmentRequestDTOsByIds", MapUtils.buildKeyValueMap("ids", ids));
    }
}
