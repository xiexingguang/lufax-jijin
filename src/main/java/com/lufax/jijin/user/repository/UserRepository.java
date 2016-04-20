package com.lufax.jijin.user.repository;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.lufax.jijin.base.repository.UserdataBaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.user.dto.UserDTO;

@Repository
public class UserRepository extends UserdataBaseRepository<UserDTO> {

    private static final String NAME_SPACE = "USERS";

    private static final String GET_USER_BY_ID = "getUserById";


    @Override
    protected String nameSpace() {
        return NAME_SPACE;
    }


    public UserDTO getUserById(Long id) {
        return query(GET_USER_BY_ID, id);
    }


    public long getFASysUserId() {
        return (Long) queryObject("getFASysUserId");
    }

    public long getSMEUserId() {
        return (Long) queryObject("getSMEUserId");
    }

    public Long getP2pUserId() {
        return (Long) queryObject("getP2pUserId");
    }
    
    public List<UserDTO> getUserInfoByIds(List<String> ids){
    	return super.queryList("getUserInfoByIds", MapUtils.buildKeyValueMap("userIdList",ids));
    }
}
