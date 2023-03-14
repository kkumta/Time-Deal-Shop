package com.kkumta.timedeal.service.order;

import com.kkumta.timedeal.api.dto.order.RequestOrderDto;
import com.kkumta.timedeal.domain.User;
import com.kkumta.timedeal.domain.UserRepository;
import com.kkumta.timedeal.domain.order.DeliveryInfo;
import com.kkumta.timedeal.domain.order.Order;
import com.kkumta.timedeal.domain.order.OrderRepository;
import com.kkumta.timedeal.domain.product.Product;
import com.kkumta.timedeal.domain.product.ProductRepository;
import com.kkumta.timedeal.exception.order.*;
import com.kkumta.timedeal.exception.product.ProductDeletedException;
import com.kkumta.timedeal.exception.product.ProductException;
import com.kkumta.timedeal.exception.product.ProductNotFoundException;
import com.kkumta.timedeal.exception.user.InvalidCredentialsException;
import com.kkumta.timedeal.exception.user.LoginInfoNotFoundException;
import java.time.LocalDateTime;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderServiceImpl implements OrderService {
    
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    
    private final HttpSession httpSession;
    
    @Override
    @Transactional
    public Long orderProduct(RequestOrderDto requestDto)
        throws ProductException, OrderException {
        
        // 구매자 유효성 검증
        Object userName = httpSession.getAttribute("NAME");
        Object userType = httpSession.getAttribute("TYPE");
        
        if (userName == null || userType == null) {
            throw new LoginInfoNotFoundException();
        }
        
        if (!userType.toString().equals("USER")) {
            throw new InvalidCredentialsException("USER 권한으로 로그인되지 않았습니다.");
        }
        
        User buyer = userRepository.findByName(userName.toString())
            .orElseThrow(() -> new RuntimeException("계정 정보가 유효하지 않습니다."));
        
        // 상품 유효성 검증
        Product product = productRepository.findById(requestDto.getProductId())
            .orElseThrow(() -> new ProductNotFoundException());
        if (product.getIsDeleted()) {
            throw new ProductDeletedException();
        }
        LocalDateTime now = LocalDateTime.now();
        if (product.getOpenDate().isAfter(now) || product.getCloseDate().isBefore(now)) {
            throw new OrderTimeException();
        }
        if (requestDto.getQuantity() > product.getMaximumPurchaseQuantity()) {
            throw new MaxQuantityExceededException();
        }
        if (requestDto.getQuantity() > product.getQuantity()) {
            throw new StockExceededException();
        }
        
        Long userId = buyer.getId();
        DeliveryInfo deliveryInfo = new DeliveryInfo(requestDto.getReceiverName(),
                                                     requestDto.getAddress(),
                                                     requestDto.getReceiverContact());
        Long quantity = requestDto.getQuantity();
        
        return orderRepository.save(Order.createOrder(product, userId, deliveryInfo, quantity))
            .getId();
    }
}