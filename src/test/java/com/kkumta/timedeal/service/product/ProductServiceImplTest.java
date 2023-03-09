package com.kkumta.timedeal.service.product;

import static org.junit.jupiter.api.Assertions.*;

import com.kkumta.timedeal.api.dto.product.RequestAddProductDto;
import com.kkumta.timedeal.api.dto.user.RequestLoginDto;
import com.kkumta.timedeal.api.dto.user.RequestSignUpDto;
import com.kkumta.timedeal.domain.Product;
import com.kkumta.timedeal.domain.ProductRepository;
import com.kkumta.timedeal.domain.User;
import com.kkumta.timedeal.domain.UserRepository;
import com.kkumta.timedeal.service.user.LoginService;
import com.kkumta.timedeal.service.user.UserService;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceImplTest {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private LoginService loginService;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @AfterEach
    void clear() {
        productRepository.deleteAll();
        userRepository.deleteAll();
    }
    
    @Test
    @DisplayName("상품 추가 성공")
    void addProduct() {
        User seller = createUser();
        loginService.login(new RequestLoginDto(seller.getEmail(), seller.getPassword()));
        LocalDateTime openDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime closeDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
            .plusMinutes(10);
        RequestAddProductDto requestDto = new RequestAddProductDto(
            "test product", 100000L, "test explanation!!\nhihihihi", 100L, 10L, openDate,
            closeDate);
        Long productId = productService.addProduct(requestDto);
        
        Product product = productRepository.findById(productId).get();
        assertEquals(requestDto.getName(), product.getName());
        assertEquals(requestDto.getPrice(), product.getPrice());
        assertEquals(requestDto.getExplanation(), product.getExplanation());
        assertEquals(requestDto.getQuantity(), product.getQuantity());
        assertEquals(requestDto.getMaximumPurchaseQuantity(), product.getMaximumPurchaseQuantity());
        assertEquals(requestDto.getOpenDate(), product.getOpenDate());
        assertEquals(requestDto.getCloseDate(), product.getCloseDate());
    }
    
    // 회원 등록
    private User createUser() {
        RequestSignUpDto requestSignUpDto = new RequestSignUpDto("test name", "test@test.com",
                                                                 "testtest123", "ADMIN",
                                                                 "01000000000",
                                                                 "객체지향도 Java시 Spring동");
        
        return userRepository.findById(userService.signUp(requestSignUpDto)).get();
    }
}