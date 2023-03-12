package com.kkumta.timedeal.service.product;

import com.kkumta.timedeal.api.dto.product.*;
import com.kkumta.timedeal.exception.product.ProductException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    
    Long addProduct(RequestAddProductDto requestDto) throws ProductException;
    
    ResponseProductDto getProductInfo(Long id) throws ProductException;
    
    void deleteProduct(Long id) throws ProductException;
    
    ResponseProductDto updateProduct(Long id, RequestUpdateProductDto requestDto)
        throws ProductException;
    
    Page<ResponseProductListDto> getProducts(String sortCondition, Pageable pageable)
        throws ProductException;
}