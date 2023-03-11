package com.kkumta.timedeal.service.product;

import static org.junit.jupiter.api.Assertions.*;

import com.kkumta.timedeal.api.dto.product.RequestAddProductDto;
import com.kkumta.timedeal.api.dto.product.ResponseProductDto;
import com.kkumta.timedeal.api.dto.user.RequestLoginDto;
import com.kkumta.timedeal.api.dto.user.RequestSignUpDto;
import com.kkumta.timedeal.domain.Product;
import com.kkumta.timedeal.domain.ProductRepository;
import com.kkumta.timedeal.domain.User;
import com.kkumta.timedeal.domain.UserRepository;
import com.kkumta.timedeal.exception.product.ProductNotFoundException;
import com.kkumta.timedeal.exception.user.LoginInfoNotFoundException;
import com.kkumta.timedeal.service.user.LoginService;
import com.kkumta.timedeal.service.user.UserService;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
    
    @Autowired
    private HttpSession session;
    
    @AfterEach
    void clear() {
        productRepository.deleteAll();
        userRepository.deleteAll();
    }
    
    @Test
    @DisplayName("상품 추가 성공")
    void addProductSuccess() {
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
    
    @Test
    @DisplayName("상품 개별 조회 성공")
    void getProductInfoSuccess() {
        LocalDateTime openDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime closeDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
            .plusMinutes(10);
        RequestAddProductDto requestDto = new RequestAddProductDto(
            "test product", 100000L, "test explanation!!\nhihihihi", 100L, 10L, openDate,
            closeDate);
        Long productId = addProduct(requestDto);
        
        ResponseProductDto responseDto = productService.getProductInfo(productId);
        assertEquals("test name", responseDto.getSellerName());
        assertEquals(requestDto.getName(), responseDto.getProductName());
        assertEquals(requestDto.getPrice(), responseDto.getPrice());
        assertEquals(requestDto.getExplanation(), responseDto.getExplanation());
        assertEquals(requestDto.getQuantity(), responseDto.getQuantity());
        assertEquals(requestDto.getMaximumPurchaseQuantity(),
                     responseDto.getMaximumPurchaseQuantity());
        assertEquals(requestDto.getOpenDate(), responseDto.getOpenDate());
        assertEquals(requestDto.getCloseDate(), responseDto.getCloseDate());
        assertEquals(false, responseDto.getIsSellingPaused());
    }
    
    @Test
    @DisplayName("상품 개별 조회 실패 - 세션 정보 없음")
    void getProductInfoFailWithSessionInfo() {
        LocalDateTime openDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime closeDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
            .plusMinutes(10);
        RequestAddProductDto requestDto = new RequestAddProductDto(
            "test product", 100000L, "test explanation!!\nhihihihi", 100L, 10L, openDate,
            closeDate);
        Long productId = addProduct(requestDto);
        session.removeAttribute("NAME");
        session.removeAttribute("TYPE");
        Assertions.assertThrows(LoginInfoNotFoundException.class, () -> {
            productService.getProductInfo(productId);
        });
    }
    
    @Test
    @DisplayName("상품 개별 조회 실패 - 세션의 NAME Attribute 유효하지 않음")
    void getProductInfoFailWithWrongName() {
        LocalDateTime openDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime closeDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
            .plusMinutes(10);
        RequestAddProductDto requestDto = new RequestAddProductDto(
            "test product", 100000L, "test explanation!!\nhihihihi", 100L, 10L, openDate,
            closeDate);
        Long productId = addProduct(requestDto);
        session.setAttribute("NAME", "wrong name");
        Assertions.assertThrows(RuntimeException.class, () -> {
            productService.getProductInfo(productId);
        });
    }
    
    @Test
    @DisplayName("상품 개별 조회 실패 - 세션의 TYPE Attribute 유효하지 않음")
    void getProductInfoFailWithWrongType() {
        LocalDateTime openDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime closeDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
            .plusMinutes(10);
        RequestAddProductDto requestDto = new RequestAddProductDto(
            "test product", 100000L, "test explanation!!\nhihihihi", 100L, 10L, openDate,
            closeDate);
        Long productId = addProduct(requestDto);
        session.setAttribute("TYPE", "CUSTOMER");
        Assertions.assertThrows(RuntimeException.class, () -> {
            productService.getProductInfo(productId);
        });
    }
    
    @Test
    @DisplayName("상품 개별 조회 실패 - 존재하지 않는 상품")
    void getProductInfoFailWithWrongId() {
        LocalDateTime openDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime closeDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
            .plusMinutes(10);
        RequestAddProductDto requestDto = new RequestAddProductDto(
            "test product", 100000L, "test explanation!!\nhihihihi", 100L, 10L, openDate,
            closeDate);
        Long productId = addProduct(requestDto);
        Assertions.assertThrows(ProductNotFoundException.class, () -> {
            productService.getProductInfo(productId + 1);
        });
    }
    
    @Test
    @DisplayName("상품 개별 조회 실패 - 삭제된 상품")
    void getProductInfoFailWithDeletedProduct() {
        LocalDateTime openDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime closeDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
            .plusMinutes(10);
        RequestAddProductDto requestDto = new RequestAddProductDto(
            "test product", 100000L, "test explanation!!\nhihihihi", 100L, 10L, openDate,
            closeDate);
        Long productId = addProduct(requestDto);
        productRepository.deleteById(productId);
        Assertions.assertThrows(RuntimeException.class, () -> {
            productService.getProductInfo(productId);
        });
    }
    
    // 회원 등록
    private User createUser() {
        RequestSignUpDto requestSignUpDto = new RequestSignUpDto("test name", "test@test.com",
                                                                 "testtest123", "ADMIN",
                                                                 "01000000000",
                                                                 "객체지향도 Java시 Spring동");
        
        return userRepository.findById(userService.signUp(requestSignUpDto)).get();
    }
    
    // 상품 추가
    private Long addProduct(RequestAddProductDto requestDto) {
        User seller = createUser();
        loginService.login(new RequestLoginDto(seller.getEmail(), seller.getPassword()));
        Long productId = productService.addProduct(requestDto);
        
        return productId;
    }
}