package com.kkumta.timedeal.service.product;

import com.kkumta.timedeal.api.dto.product.RequestAddProductDto;
import com.kkumta.timedeal.api.dto.product.ResponseProductDto;
import com.kkumta.timedeal.exception.product.ProductException;

public interface ProductService {
    
    Long addProduct(RequestAddProductDto requestDto) throws ProductException;
    
    ResponseProductDto getProductInfo(Long id) throws ProductException;
    
    void deleteProduct(Long id) throws ProductException;
}