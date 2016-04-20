package com.lufax.jijin.fundation.gson;


import com.lufax.jijin.fundation.dto.JijinAccountDTO;

public class JijinAccountGson {
    private Long userId;
    private String instId;

    public JijinAccountGson(JijinAccountDTO jijinAccountDTO) {
        this.userId = jijinAccountDTO.getUserId();
        this.instId = jijinAccountDTO.getInstId();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getInstId() {
        return instId;
    }

    public void setInstId(String instId) {
        this.instId = instId;
    }
}
