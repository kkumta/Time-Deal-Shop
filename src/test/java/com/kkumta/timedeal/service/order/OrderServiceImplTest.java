package com.kkumta.timedeal.service.order;

import static org.junit.jupiter.api.Assertions.*;

import com.kkumta.timedeal.api.dto.order.RequestOrderDto;
import com.kkumta.timedeal.api.dto.order.ResponseOrderListDto;
import com.kkumta.timedeal.api.dto.product.RequestAddProductDto;
import com.kkumta.timedeal.api.dto.user.RequestLoginDto;
import com.kkumta.timedeal.api.dto.user.RequestSignUpDto;
import com.kkumta.timedeal.domain.User;
import com.kkumta.timedeal.domain.UserRepository;
import com.kkumta.timedeal.domain.order.OrderRepository;
import com.kkumta.timedeal.domain.product.ProductRepository;
import com.kkumta.timedeal.exception.order.OrderException;
import com.kkumta.timedeal.exception.product.ProductException;
import com.kkumta.timedeal.exception.user.UserException;
import com.kkumta.timedeal.service.product.ProductService;
import com.kkumta.timedeal.service.user.LoginService;
import com.kkumta.timedeal.service.user.UserService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
class OrderServiceImplTest {
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private LoginService loginService;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @AfterEach
    void clear() {
        userRepository.deleteAll();
        orderRepository.deleteAll();
        productRepository.deleteAll();
    }
    
    @Test
    @DisplayName("상품 주문")
    void orderProduct() throws ProductException, OrderException, InterruptedException, UserException {
        
        // given
        Long productId = addProductWithLogin();
        User buyer = createUser(new RequestSignUpDto("test buyer", "testbuyer@test.com",
                                                     "testtest123", "USER",
                                                     "010011111111",
                                                     "객체지향도 Java시 JPA동"));
        loginService.login(new RequestLoginDto(buyer.getEmail(), buyer.getPassword()));
        
        RequestOrderDto requestDto = RequestOrderDto.builder()
            .productId(productId)
            .receiverName("receiverName")
            .address("address")
            .receiverContact("01077777777")
            .quantity(5L)
            .build();
        
        // when
        orderService.orderProduct(requestDto);
        
        // then
        assertEquals(95L, productService.getProductInfo(productId).getQuantity());
    }
    
    @Test
    @DisplayName("주문 목록 조회")
    void getOrders() throws ProductException, OrderException, UserException {
        
        // given
        Long productId = addProductWithLogin();
        User buyer = createUser(new RequestSignUpDto("test buyer", "testbuyer@test.com",
                                                     "testtest123", "USER",
                                                     "010011111111",
                                                     "객체지향도 Java시 JPA동"));
        loginService.login(new RequestLoginDto(buyer.getEmail(), buyer.getPassword()));
        
        RequestOrderDto requestDto = RequestOrderDto.builder()
            .productId(productId)
            .receiverName("receiverName")
            .address("address")
            .receiverContact("01077777777")
            .quantity(1L)
            .build();
        for (int i = 0; i < 11; i++) {
            orderService.orderProduct(requestDto);
        }
        LocalDateTime now = LocalDateTime.now();
        String nowToString = now.format(DateTimeFormatter.ofPattern("yyMMdd"));
        
        // when
        Page<ResponseOrderListDto> orders = orderService.getOrders(buyer.getId(), nowToString,
                                                                   nowToString,
                                                                   PageRequest.of(0, 1));
        
        // then
        Assertions.assertEquals(11, orders.getTotalElements());
        Assertions.assertEquals(2, orders.getTotalPages());
    }
    
    // 회원 등록
    private User createUser(RequestSignUpDto requestDto) throws UserException {
        
        return userRepository.findById(userService.signUp(requestDto)).get();
    }
    
    // 상품 추가
    private Long addProductWithLogin() throws ProductException, UserException {
        
        LocalDateTime openDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime closeDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
            .plusMinutes(10);
        
        RequestAddProductDto addProductDto = RequestAddProductDto.builder()
            .name("test product")
            .price(100000L)
            .explanation("test explanation!!\nhihihihi")
            .quantity(100L)
            .maximumPurchaseQuantity(10L)
            .openDate(openDate)
            .closeDate(closeDate)
            .build();
        
        User seller = createUser(new RequestSignUpDto("test name", "test@test.com",
                                                      "testtest123", "ADMIN",
                                                      "01000000000",
                                                      "객체지향도 Java시 Spring동"));
        loginService.login(new RequestLoginDto(seller.getEmail(), seller.getPassword()));
        Long productId = productService.addProduct(addProductDto);
        
        return productId;
    }
}