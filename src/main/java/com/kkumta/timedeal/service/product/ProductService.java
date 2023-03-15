package com.kkumta.timedeal.service.product;

import com.kkumta.timedeal.api.dto.product.*;
import com.kkumta.timedeal.api.dto.user.ResponseUserListDto;
import com.kkumta.timedeal.exception.product.ProductException;
import com.kkumta.timedeal.exception.user.LoginInfoNotFoundException;
import com.kkumta.timedeal.exception.user.UserException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    
    Long addProduct(RequestAddProductDto requestDto) throws ProductException, UserException;
    
    ResponseProductDto getProductInfo(Long id) throws ProductException, LoginInfoNotFoundException;
    
    void deleteProduct(Long id) throws ProductException;
    
    ResponseProductDto updateProduct(Long id, RequestUpdateProductDto requestDto)
        throws ProductException;
    
    Page<ResponseProductListDto> getProducts(String sortCondition, Pageable pageable)
        throws ProductException, LoginInfoNotFoundException;
    
    Page<ResponseProductListDto> getMyProducts(String startDate, String endDate, Pageable pageable)
        throws UserException;
    
    Page<ResponseUserListDto> getUsersByProduct(Long id, Pageable pageable)
        throws ProductException;
}