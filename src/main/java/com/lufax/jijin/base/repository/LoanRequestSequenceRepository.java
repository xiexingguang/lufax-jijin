package com.lufax.jijin.base.repository;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.lufax.jijin.base.dto.LoanRequestSequenceDTO;

@Repository
public class LoanRequestSequenceRepository extends BusdataBaseRepository<LoanRequestSequenceDTO> {

    private static final String NAME_SPACE = "LOAN_REQUEST_SEQUENCE";
    private static final String INSERT_LOAN_SEQUENCE_REQUEST = "insertLoanRequestSequence";
    private static final String GET_MAX_SEQUENCE = "getMaxSequence";

    @Override
    protected String nameSpace() {
        return NAME_SPACE;
    }

    
    public LoanRequestSequenceDTO insertLoanRequestSequenceDTO(LoanRequestSequenceDTO loanRequestSequenceDTO) {
        return super.insert(INSERT_LOAN_SEQUENCE_REQUEST, loanRequestSequenceDTO);
    }

    public Long findMaxIdBefore(Date createdAt) {
        Long maxId = (Long) queryObject(GET_MAX_SEQUENCE, createdAt);
        if (maxId == null) {
            return 0L;
        }
        return maxId;
    }
}
