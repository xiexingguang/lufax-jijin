package com.lufax.jijin.fundation.gson;

import java.util.List;

public class JijinAccountResultGson extends BaseGson {

    private List<JijinAccountGson> list;

    public List<JijinAccountGson> getList() {
        return list;
    }

    public void setList(List<JijinAccountGson> list) {
        this.list = list;
    }
}
