package com.kkumta.timedeal.service.product;

import com.kkumta.timedeal.api.dto.product.RequestAddProductDto;

public interface ProductService {
    
    Long addProduct(RequestAddProductDto requestDto);
    
}