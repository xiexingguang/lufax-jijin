package com.lufax.jijin.product.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.lufax.jijin.base.repository.TrddataBaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.product.constant.ProductSourceType;
import com.lufax.jijin.product.constant.ProductType;
import com.lufax.jijin.product.dto.ProductDTO;

@Repository
public class ProductRepository extends TrddataBaseRepository<ProductDTO> {
    @Override
    protected String nameSpace() {
        return "Product";
    }

    public ProductDTO getById(long productId) {
        return query("getProductById", productId);
    }

    public ProductDTO getByCode(String productCode) {
        return query("getProductByRequestCode", MapUtils.buildKeyValueMap("code", productCode));
    }

    public ProductDTO getByIdAndSourceType(long productId, String sourceType) {
        return query("getProductId", MapUtils.buildKeyValueMap("id", productId, "sourceType", sourceType));
    }

    public ProductDTO getBySourceIdAndProductSourceType(long id, ProductSourceType sourceType, ProductType productType) {
        return query("getProductBySourceId", MapUtils.buildKeyValueMap("sourceId", id, "sourceType", sourceType.getCode(), "productType", productType.name()));
    }
    
    public List<ProductDTO> getByIds(List<Long> ids) {
    	if(ids.isEmpty()){
    		return new ArrayList<ProductDTO>();
    	}
        return queryList("getProductByIds", MapUtils.buildKeyValueMap("ids", ids));
    }
    
    public List<ProductDTO> getByCodes(Collection<String> codes) {
    	if(codes.isEmpty()){
    		return new ArrayList<ProductDTO>();
    	}
        return queryList("getProductByCodes", MapUtils.buildKeyValueMap("codes", new ArrayList<String>(new HashSet<String>(codes))));
    }
}
