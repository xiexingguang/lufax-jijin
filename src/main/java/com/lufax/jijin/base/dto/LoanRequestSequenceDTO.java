package com.lufax.jijin.base.dto;

import java.util.Date;

public class LoanRequestSequenceDTO extends BaseDTO {

    private Date createdAt;

    public LoanRequestSequenceDTO() {
    }

    public LoanRequestSequenceDTO(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
