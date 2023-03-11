package com.kkumta.timedeal.service.product;

import static org.junit.jupiter.api.Assertions.*;

import com.kkumta.timedeal.api.dto.product.*;
import com.kkumta.timedeal.api.dto.user.RequestLoginDto;
import com.kkumta.timedeal.api.dto.user.RequestSignUpDto;
import com.kkumta.timedeal.domain.Product;
import com.kkumta.timedeal.domain.ProductRepository;
import com.kkumta.timedeal.domain.User;
import com.kkumta.timedeal.domain.UserRepository;
import com.kkumta.timedeal.exception.product.*;
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
    
    final LocalDateTime openDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    final LocalDateTime closeDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        .plusMinutes(10);
    
    @AfterEach
    void clear() {
        productRepository.deleteAll();
        userRepository.deleteAll();
    }
    
    @Test
    @DisplayName("상품 추가 성공")
    void addProductSuccess() throws ProductException {
        User seller = createUser(new RequestSignUpDto("test name", "test@test.com",
                                                      "testtest123", "ADMIN",
                                                      "01000000000",
                                                      "객체지향도 Java시 Spring동"));
        loginService.login(new RequestLoginDto(seller.getEmail(), seller.getPassword()));
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
    void getProductInfoSuccess() throws ProductException {
        RequestAddProductDto requestDto =
            RequestAddProductDto.builder()
                .name("test product")
                .price(100000L)
                .explanation("test explanation!!\nhihihihi")
                .quantity(100L)
                .maximumPurchaseQuantity(10L)
                .openDate(openDate)
                .closeDate(closeDate)
                .build();
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
    void getProductInfoFailWithSessionInfo() throws ProductException {
        RequestAddProductDto requestDto =
            RequestAddProductDto.builder()
                .name("test product")
                .price(100000L)
                .explanation("test explanation!!\nhihihihi")
                .quantity(100L)
                .maximumPurchaseQuantity(10L)
                .openDate(openDate)
                .closeDate(closeDate)
                .build();
        Long productId = addProduct(requestDto);
        session.removeAttribute("NAME");
        session.removeAttribute("TYPE");
        Assertions.assertThrows(LoginInfoNotFoundException.class, () -> {
            productService.getProductInfo(productId);
        });
    }
    
    @Test
    @DisplayName("상품 개별 조회 실패 - 세션의 NAME Attribute 유효하지 않음")
    void getProductInfoFailWithWrongName() throws ProductException {
        RequestAddProductDto requestDto =
            RequestAddProductDto.builder()
                .name("test product")
                .price(100000L)
                .explanation("test explanation!!\nhihihihi")
                .quantity(100L)
                .maximumPurchaseQuantity(10L)
                .openDate(openDate)
                .closeDate(closeDate)
                .build();
        Long productId = addProduct(requestDto);
        session.setAttribute("NAME", "wrong name");
        Assertions.assertThrows(RuntimeException.class, () -> {
            productService.getProductInfo(productId);
        });
    }
    
    @Test
    @DisplayName("상품 개별 조회 실패 - 세션의 TYPE Attribute 유효하지 않음")
    void getProductInfoFailWithWrongType() throws ProductException {
        RequestAddProductDto requestDto =
            RequestAddProductDto.builder()
                .name("test product")
                .price(100000L)
                .explanation("test explanation!!\nhihihihi")
                .quantity(100L)
                .maximumPurchaseQuantity(10L)
                .openDate(openDate)
                .closeDate(closeDate)
                .build();
        Long productId = addProduct(requestDto);
        session.setAttribute("TYPE", "CUSTOMER");
        Assertions.assertThrows(RuntimeException.class, () -> {
            productService.getProductInfo(productId);
        });
    }
    
    @Test
    @DisplayName("상품 개별 조회 실패 - 존재하지 않는 상품")
    void getProductInfoFailWithWrongId() throws ProductException {
        RequestAddProductDto requestDto =
            RequestAddProductDto.builder()
                .name("test product")
                .price(100000L)
                .explanation("test explanation!!\nhihihihi")
                .quantity(100L)
                .maximumPurchaseQuantity(10L)
                .openDate(openDate)
                .closeDate(closeDate)
                .build();
        Long productId = addProduct(requestDto);
        Assertions.assertThrows(ProductNotFoundException.class, () -> {
            productService.getProductInfo(productId + 1);
        });
    }
    
    @Test
    @DisplayName("상품 개별 조회 실패 - 삭제된 상품")
    void getProductInfoFailWithDeletedProduct() throws ProductException {
        RequestAddProductDto requestDto =
            RequestAddProductDto.builder()
                .name("test product")
                .price(100000L)
                .explanation("test explanation!!\nhihihihi")
                .quantity(100L)
                .maximumPurchaseQuantity(10L)
                .openDate(openDate)
                .closeDate(closeDate)
                .build();
        Long productId = addProduct(requestDto);
        productRepository.deleteById(productId);
        Assertions.assertThrows(ProductNotFoundException.class, () -> {
            productService.getProductInfo(productId);
        });
    }
    
    @Test
    @DisplayName("상품 개별 삭제 성공")
    void deleteProductSuccess() throws ProductException {
        RequestAddProductDto requestDto =
            RequestAddProductDto.builder()
                .name("test product")
                .price(100000L)
                .explanation("test explanation!!\nhihihihi")
                .quantity(100L)
                .maximumPurchaseQuantity(10L)
                .openDate(openDate)
                .closeDate(closeDate)
                .build();
        Long productId = addProduct(requestDto);
        productService.deleteProduct(productId);
        Assertions.assertThrows(ProductDeletedException.class, () -> {
            productService.getProductInfo(productId);
        });
    }
    
    @Test
    @DisplayName("상품 개별 삭제 실패 - 판매자가 로그인하지 않음")
    void deleteProductFailWithSellerMismatch() throws ProductException {
        RequestAddProductDto requestDto =
            RequestAddProductDto.builder()
                .name("test product")
                .price(100000L)
                .explanation("test explanation!!\nhihihihi")
                .quantity(100L)
                .maximumPurchaseQuantity(10L)
                .openDate(openDate)
                .closeDate(closeDate)
                .build();
        Long productId = addProduct(requestDto);
        
        User newUser = createUser(new RequestSignUpDto("test2 name", "test2@test.com",
                                                       "testtest123", "USER",
                                                       "01011111111",
                                                       "객체지향도 Java시 JPA동"));
        
        loginService.login(new RequestLoginDto(newUser.getEmail(), newUser.getPassword()));
        
        Assertions.assertThrows(SellerMismatchException.class, () -> {
            productService.deleteProduct(productId);
        });
    }
    
    @Test
    @DisplayName("상품 개별 수정 성공")
    void updateProduct() throws ProductException {
        RequestAddProductDto requestAddDto =
            RequestAddProductDto.builder()
                .name("test product")
                .price(100000L)
                .explanation("test explanation!!\nhihihihi")
                .quantity(100L)
                .maximumPurchaseQuantity(10L)
                .openDate(openDate)
                .closeDate(closeDate)
                .build();
        Long productId = addProduct(requestAddDto);
        
        RequestUpdateProductDto requestUpdateDto =
            RequestUpdateProductDto.builder()
                .name("real product")
                .price(200000L)
                .explanation("real explanation!")
                .quantity(30L)
                .maximumPurchaseQuantity(2L)
                .openDate(openDate.plusMinutes(30))
                .closeDate(closeDate.plusMinutes(30))
                .isSellingPaused(true)
                .build();
        productService.updateProduct(productId, requestUpdateDto);
        ResponseProductDto responseDto = productService.getProductInfo(productId);
        
        Assertions.assertEquals(responseDto.getProductName(), requestUpdateDto.getName());
        Assertions.assertEquals(responseDto.getPrice(), requestUpdateDto.getPrice());
        Assertions.assertEquals(responseDto.getExplanation(), requestUpdateDto.getExplanation());
        Assertions.assertEquals(responseDto.getQuantity(), requestUpdateDto.getQuantity());
        Assertions.assertEquals(responseDto.getMaximumPurchaseQuantity(),
                                requestUpdateDto.getMaximumPurchaseQuantity());
        Assertions.assertEquals(responseDto.getOpenDate(), requestUpdateDto.getOpenDate());
        Assertions.assertEquals(responseDto.getCloseDate(), requestUpdateDto.getCloseDate());
        Assertions.assertEquals(true, requestUpdateDto.getIsSellingPaused());
    }
    
    @Test
    @DisplayName("상품 개별 수정 실패 - InvalidDate")
    void updateProductFail() throws ProductException {
        RequestAddProductDto requestAddDto =
            RequestAddProductDto.builder()
                .name("test product")
                .price(100000L)
                .explanation("test explanation!!\nhihihihi")
                .quantity(100L)
                .maximumPurchaseQuantity(10L)
                .openDate(openDate)
                .closeDate(closeDate)
                .build();
        Long productId = addProduct(requestAddDto);
        
        RequestUpdateProductDto requestUpdateDto =
            RequestUpdateProductDto.builder()
                .name("real product")
                .price(200000L)
                .explanation("real explanation!")
                .quantity(30L)
                .maximumPurchaseQuantity(2L)
                .openDate(openDate.plusMinutes(5))
                .closeDate(closeDate)
                .isSellingPaused(true)
                .build();
        
        Assertions.assertThrows(InvalidDateException.class, () -> {
            productService.updateProduct(productId, requestUpdateDto);
        });
    }
    
    
    // 회원 등록
    private User createUser(RequestSignUpDto requestDto) {
        
        return userRepository.findById(userService.signUp(requestDto)).get();
    }
    
    // 상품 추가
    private Long addProduct(RequestAddProductDto requestDto) throws ProductException {
        User seller = createUser(new RequestSignUpDto("test name", "test@test.com",
                                                      "testtest123", "ADMIN",
                                                      "01000000000",
                                                      "객체지향도 Java시 Spring동"));
        loginService.login(new RequestLoginDto(seller.getEmail(), seller.getPassword()));
        Long productId = productService.addProduct(requestDto);
        
        return productId;
    }
}