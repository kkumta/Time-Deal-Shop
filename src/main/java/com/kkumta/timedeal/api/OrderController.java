package com.kkumta.timedeal.api;

import com.kkumta.timedeal.api.dto.order.RequestOrderDto;
import com.kkumta.timedeal.domain.product.Product;
import com.kkumta.timedeal.domain.product.ProductRepository;
import com.kkumta.timedeal.exception.order.OrderException;
import com.kkumta.timedeal.exception.product.ProductException;
import com.kkumta.timedeal.service.order.OrderService;
import java.net.URI;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/orders")
@RestController
public class OrderController {
    
    private final OrderService orderService;
    
    private final ProductRepository productRepository;
    
    @PostMapping
    public ResponseEntity<Long> orderProduct(
        @Valid @RequestBody RequestOrderDto requestDto) throws OrderException, ProductException {
        Long orderId = orderService.orderProduct(requestDto);
        Optional<Product> product = productRepository.findById(requestDto.getProductId());
        log.info("현재 남은 수량: " + product.get().getQuantity());
        return ResponseEntity.created(URI.create("orders/" + orderId)).body(orderId);
    }
}
