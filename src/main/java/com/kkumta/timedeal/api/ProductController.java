package com.kkumta.timedeal.api;

import com.kkumta.timedeal.api.dto.product.RequestAddProductDto;
import com.kkumta.timedeal.api.dto.product.RequestUpdateProductDto;
import com.kkumta.timedeal.api.dto.product.ResponseProductDto;
import com.kkumta.timedeal.api.dto.product.ResponseProductListDto;
import com.kkumta.timedeal.exception.product.ProductException;
import com.kkumta.timedeal.service.product.ProductService;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("products")
@RestController
public class ProductController {
    
    private final ProductService productService;
    
    @PostMapping
    public ResponseEntity<Long> addProduct(
        @Valid @RequestBody RequestAddProductDto requestDto) throws ProductException {
        Long productId = productService.addProduct(requestDto);
        return ResponseEntity.created(URI.create("products/" + productId)).body(productId);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ResponseProductDto> getProductInfo(@PathVariable Long id)
        throws ProductException {
        return ResponseEntity.ok(productService.getProductInfo(id));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) throws ProductException {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ResponseProductDto> updateProduct(@PathVariable Long id,
                                                            @Valid @RequestBody
                                                            RequestUpdateProductDto requestDto)
        throws ProductException {
        return ResponseEntity.ok(productService.updateProduct(id, requestDto));
    }
    
    @GetMapping
    public ResponseEntity<Page<ResponseProductListDto>> getProducts(String sortCondition,
                                                                    Pageable pageable)
        throws ProductException {
        
        return ResponseEntity.ok(productService.getProducts(sortCondition, pageable));
    }
    
    @GetMapping("/my")
    public ResponseEntity<Page<ResponseProductListDto>> getMyProducts(String startDate,
                                                                      String endDate,
                                                                      Pageable pageable) {
        
        return ResponseEntity.ok(
            productService.getMyProducts(startDate, endDate, pageable));
    }
}