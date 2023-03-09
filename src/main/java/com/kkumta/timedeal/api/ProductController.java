package com.kkumta.timedeal.api;

import com.kkumta.timedeal.api.dto.product.RequestAddProductDto;
import com.kkumta.timedeal.service.product.ProductService;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("products")
@RestController
public class ProductController {
    
    private final ProductService productService;
    
    @PostMapping
    public ResponseEntity<Long> validateUniqueName(
        @Valid @RequestBody RequestAddProductDto requestDto) {
        Long productId = productService.addProduct(requestDto);
        return ResponseEntity.created(URI.create("products/" + productId)).body(productId);
    }
}