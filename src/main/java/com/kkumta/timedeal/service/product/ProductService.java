package com.kkumta.timedeal.service.product;

import com.kkumta.timedeal.api.dto.product.RequestAddProductDto;
import com.kkumta.timedeal.api.dto.product.ResponseProductDto;

public interface ProductService {
    
    Long addProduct(RequestAddProductDto requestDto);
    
    ResponseProductDto getProductInfo(Long id);
}