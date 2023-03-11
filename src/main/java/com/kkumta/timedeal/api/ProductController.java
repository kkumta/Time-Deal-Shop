package com.kkumta.timedeal.api;

import com.kkumta.timedeal.api.dto.product.RequestAddProductDto;
import com.kkumta.timedeal.api.dto.product.ResponseProductDto;
import com.kkumta.timedeal.service.product.ProductService;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<Long> addProduct(
        @Valid @RequestBody RequestAddProductDto requestDto) {
        Long productId = productService.addProduct(requestDto);
        return ResponseEntity.created(URI.create("products/" + productId)).body(productId);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ResponseProductDto> getProductInfo(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductInfo(id));
    }
}