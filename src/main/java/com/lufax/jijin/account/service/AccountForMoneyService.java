package com.lufax.jijin.account.service;

import com.google.gson.Gson;
import com.lufax.jijin.sysFacade.caller.InterfaceCallObject;
import com.lufax.jijin.user.repository.UserRepository;
import com.lufax.jijin.ylx.dto.YLXAccountTransferRecordDTO;
import com.lufax.jijin.base.constant.AccountInterfaceConstants;
import com.lufax.jijin.base.dto.TransferRefundParam;
import com.lufax.jijin.base.dto.TransferRefundResponse;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.facade.caller.AccountInterfaceCaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountForMoneyService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
    private AccountInterfaceCaller accountInterfaceCaller;
	
	public TransferRefundResponse refundForInsurance(YLXAccountTransferRecordDTO fsa) {
        long faSysUserId = userRepository.getFASysUserId();
        InterfaceCallObject interfaceCallObject = new InterfaceCallObject()
                .withUserId(faSysUserId)
                .withUrl(AccountInterfaceConstants.getAccountTransferStandardUrl(faSysUserId))
                .addFormValue("dto", new Gson().toJson(new TransferRefundParam(fsa)));
        Logger.info(this, String.format("transfer/refund  : %s",new Gson().toJson(new TransferRefundParam(fsa))));
        return accountInterfaceCaller.post(interfaceCallObject, TransferRefundResponse.class);
	}
}
