package com.lufax.jijin.product.service;

import com.google.gson.Gson;
import com.lufax.jijin.sysFacade.gson.ListProductGson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.dao.FundLoanRequestDAO;
import com.lufax.jijin.dto.FundLoanRequestDTO;
import com.lufax.jijin.product.dto.ProductDTO;
import com.lufax.jijin.product.dto.RemoteCallerException;
import com.lufax.jijin.product.dto.UpdateStatusGson;
import com.lufax.jijin.product.gson.ProductInputGson;
import com.lufax.jijin.product.repository.ProductRepository;

@Service
public class FundLoanRequestService {

    @Autowired
    private FundLoanRequestDAO fundLoanRequestDAO;
    @Autowired
    private ProductRepository productdao;
    @Autowired
    private ProductService productService;
    
    @Transactional
    public void createFundLoanRequest(ProductInputGson fundProductGson) {
        FundLoanRequestDTO fundLoanRequestDTO = fundLoanRequestDAO.getFundLoanRequestByForeignId(fundProductGson.getForeignId());
        if (fundLoanRequestDTO != null) {
            Logger.info(this, String.format("FundLoanRequest already created by foreignId [%s]", fundProductGson.getForeignId()));
            return;
        }

        fundLoanRequestDAO.insertFundLoanRequest(new FundLoanRequestDTO(fundProductGson));
        fundLoanRequestDTO = fundLoanRequestDAO.getFundLoanRequestByForeignId(fundProductGson.getForeignId());

        ListProductGson listProductGson = new ListProductGson(fundProductGson,fundLoanRequestDTO);
        Logger.info(this, new Gson().toJson(listProductGson));
        if (!productService.createProduct(new ListProductGson(fundProductGson,fundLoanRequestDTO))) {
            throw new RemoteCallerException("Create Product Failed.");
        }
        Logger.info(this, String.format("Create Product of loan request productCode %s successfully", fundProductGson.getProductCode()));
    }
    
    public boolean confirmLoanRequestRaiseResult(UpdateStatusGson gson) {
    	FundLoanRequestDTO fundLoanRequestDTO = fundLoanRequestDAO.getFundLoanRequestByForeignId(gson.getForeignId());
    	ProductDTO product = productdao.getByCode(gson.getProductCode());
        if (fundLoanRequestDTO != null) {
        	return productService.updateProduct(product.getId(), gson.getResult());
        }else{
        	return false;
        }
    }
}
