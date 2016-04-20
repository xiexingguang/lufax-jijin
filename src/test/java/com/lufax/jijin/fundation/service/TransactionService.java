package com.lufax.jijin.fundation.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lufax.jijin.fundation.dto.JijinFrozenDetailDTO;
import com.lufax.jijin.fundation.repository.JijinFrozenDetailRepository;
import com.lufax.jijin.fundation.util.ApplicationContextUtils;

@Service
public class TransactionService {

    @Autowired
    private JijinFrozenDetailRepository jijinFrozenDetailRepository;
    
    //不回滚
    public void run1() {
    	try {
			this.runExcept1();
		} catch (Exception e) {
			System.out.println("+++++++++++++++++++++++");
		}
    }
    
	//不回滚
    public void run2() {
    	this.runExcept2();
    }
    
	//不回滚
    public void run7() {
    	try {
			this.runExcept3();
		} catch (Exception e) {
			System.out.println("+++++++++++++++++++++++");
		}
    }
    
	//不回滚
    public void run8() {
    	this.runExcept4();
    }
    
	//不回滚
    public void run5() {
    	try {
    		ApplicationContextUtils.getBean("transactionService", TransactionService.class).runExcept3();
		} catch (Exception e) {
			System.out.println("......................");
		}
    }
    
	//回滚
    public void run3() {
    	try {
    		ApplicationContextUtils.getBean("transactionService", TransactionService.class).runExcept1();
		} catch (Exception e) {
			System.out.println("---------------------");
		}
    }
    
	//回滚
    public void run4() {
    	ApplicationContextUtils.getBean("transactionService", TransactionService.class).runExcept2();
    }
    
	//回滚
    public void run6() {
    	ApplicationContextUtils.getBean("transactionService", TransactionService.class).runExcept4();
    }
    
	//回滚
    public void run9() {
    	try {
    		ApplicationContextUtils.getBean("transactionService", TransactionService.class).runExcept1();
		} catch (Exception e) {
			throw new RuntimeException();
		}
    }
    
	//回滚
    public void run10() throws Exception {
    	ApplicationContextUtils.getBean("transactionService", TransactionService.class).runExcept1();
    }
    
    @Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void runExcept1() throws Exception {
		JijinFrozenDetailDTO dto = new JijinFrozenDetailDTO("06", 208269L, BigDecimal.valueOf(100.23), BigDecimal.valueOf(100.24), BigDecimal.valueOf(100.25), "test3");
		jijinFrozenDetailRepository.insertJijinFrozenDetail(dto);
		throw new Exception("test3 exception.");
	}
    
    @Transactional(propagation=Propagation.REQUIRED, rollbackFor=RuntimeException.class)
	public void runExcept2() {
		JijinFrozenDetailDTO dto = new JijinFrozenDetailDTO("06", 208269L, BigDecimal.valueOf(100.23), BigDecimal.valueOf(100.24), BigDecimal.valueOf(100.25), "test3");
		jijinFrozenDetailRepository.insertJijinFrozenDetail(dto);
		throw new RuntimeException("test3 exception.");
	}
    
    @Transactional(propagation=Propagation.REQUIRED)
	public void runExcept3() throws Exception {
		JijinFrozenDetailDTO dto = new JijinFrozenDetailDTO("06", 208269L, BigDecimal.valueOf(100.23), BigDecimal.valueOf(100.24), BigDecimal.valueOf(100.25), "test3");
		jijinFrozenDetailRepository.insertJijinFrozenDetail(dto);
		throw new Exception("test3 exception.");
	}
    
    @Transactional(propagation=Propagation.REQUIRED)
	public void runExcept4() {
		JijinFrozenDetailDTO dto = new JijinFrozenDetailDTO("06", 208269L, BigDecimal.valueOf(100.23), BigDecimal.valueOf(100.24), BigDecimal.valueOf(100.25), "test3");
		jijinFrozenDetailRepository.insertJijinFrozenDetail(dto);
		throw new RuntimeException("test3 exception.");
	}
    
    public static TransactionService getTransactionServiceInstance() {
    	return ApplicationContextUtils.getBean("transactionService", TransactionService.class);
    }
    
    public boolean isSame() {
    	return this == getTransactionServiceInstance();
    }
}
