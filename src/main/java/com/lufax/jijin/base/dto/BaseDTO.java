package com.lufax.jijin.base.dto;


import com.lufax.jijin.base.utils.BeanUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseDTO implements Serializable {

    private static final long serialVersionUID = -315506079592557582L;

    private long id;

    private Map<String, Object> setterMap;


    protected BaseDTO() {

    }

    @Override
    public String toString() {
        return BeanUtils.bean2map(this).toString();
    }

    public long id() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public long getId() {
		return id;
	}

	public synchronized void initSetterMap() {
        if (setterMap == null) {
            setterMap = new HashMap<String, Object>();
        }
    }

    public Map<String, Object> getSetterMap() {
        return setterMap;
    }
}
