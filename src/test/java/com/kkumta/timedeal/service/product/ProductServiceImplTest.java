package com.kkumta.timedeal.service.product;

import static org.junit.jupiter.api.Assertions.*;

import com.kkumta.timedeal.api.dto.order.RequestOrderDto;
import com.kkumta.timedeal.api.dto.product.*;
import com.kkumta.timedeal.api.dto.user.RequestLoginDto;
import com.kkumta.timedeal.api.dto.user.RequestSignUpDto;
import com.kkumta.timedeal.api.dto.user.ResponseUserListDto;
import com.kkumta.timedeal.domain.order.OrderRepository;
import com.kkumta.timedeal.domain.product.*;
import com.kkumta.timedeal.domain.User;
import com.kkumta.timedeal.domain.UserRepository;
import com.kkumta.timedeal.exception.order.OrderException;
import com.kkumta.timedeal.exception.product.*;
import com.kkumta.timedeal.exception.user.LoginInfoNotFoundException;
import com.kkumta.timedeal.exception.user.UserException;
import com.kkumta.timedeal.service.order.OrderService;
import com.kkumta.timedeal.service.user.LoginService;
import com.kkumta.timedeal.service.user.UserService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
class ProductServiceImplTest {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private LoginService loginService;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private HttpSession session;
    
    final LocalDateTime openDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    final LocalDateTime closeDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        .plusMinutes(10);
    
    @AfterEach
    void clear() {
        orderRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();
    }
    
    @Test
    @DisplayName("상품 추가 성공")
    void addProductSuccess() throws ProductException, UserException {
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
    void getProductInfoSuccess() throws ProductException, UserException {
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
        Long productId = addProductWithLogin(requestDto);
        
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
    void getProductInfoFailWithSessionInfo() throws ProductException, UserException {
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
        Long productId = addProductWithLogin(requestDto);
        session.removeAttribute("NAME");
        session.removeAttribute("TYPE");
        assertThrows(LoginInfoNotFoundException.class, () -> {
            productService.getProductInfo(productId);
        });
    }
    
    @Test
    @DisplayName("상품 개별 조회 실패 - 세션의 NAME Attribute 유효하지 않음")
    void getProductInfoFailWithWrongName() throws ProductException, UserException {
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
        Long productId = addProductWithLogin(requestDto);
        session.setAttribute("NAME", "wrong name");
        assertThrows(RuntimeException.class, () -> {
            productService.getProductInfo(productId);
        });
    }
    
    @Test
    @DisplayName("상품 개별 조회 실패 - 세션의 TYPE Attribute 유효하지 않음")
    void getProductInfoFailWithWrongType() throws ProductException, UserException {
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
        Long productId = addProductWithLogin(requestDto);
        session.setAttribute("TYPE", "CUSTOMER");
        assertThrows(RuntimeException.class, () -> {
            productService.getProductInfo(productId);
        });
    }
    
    @Test
    @DisplayName("상품 개별 조회 실패 - 존재하지 않는 상품")
    void getProductInfoFailWithWrongId() throws ProductException, UserException {
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
        Long productId = addProductWithLogin(requestDto);
        assertThrows(ProductNotFoundException.class, () -> {
            productService.getProductInfo(productId + 1);
        });
    }
    
    @Test
    @DisplayName("상품 개별 조회 실패 - 삭제된 상품")
    void getProductInfoFailWithDeletedProduct() throws ProductException, UserException {
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
        Long productId = addProductWithLogin(requestDto);
        productRepository.deleteById(productId);
        assertThrows(ProductNotFoundException.class, () -> {
            productService.getProductInfo(productId);
        });
    }
    
    @Test
    @DisplayName("상품 개별 삭제 성공")
    void deleteProductSuccess() throws ProductException, UserException {
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
        Long productId = addProductWithLogin(requestDto);
        productService.deleteProduct(productId);
        assertThrows(ProductDeletedException.class, () -> {
            productService.getProductInfo(productId);
        });
    }
    
    @Test
    @DisplayName("상품 개별 삭제 실패 - 판매자가 로그인하지 않음")
    void deleteProductFailWithSellerMismatch() throws ProductException, UserException {
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
        Long productId = addProductWithLogin(requestDto);
        
        User newUser = createUser(new RequestSignUpDto("test2 name", "test2@test.com",
                                                       "testtest123", "USER",
                                                       "01011111111",
                                                       "객체지향도 Java시 JPA동"));
        
        loginService.login(new RequestLoginDto(newUser.getEmail(), newUser.getPassword()));
        
        assertThrows(SellerMismatchException.class, () -> {
            productService.deleteProduct(productId);
        });
    }
    
    @Test
    @DisplayName("상품 개별 수정 성공")
    void updateProduct() throws ProductException, UserException {
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
        Long productId = addProductWithLogin(requestAddDto);
        
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
    void updateProductFail() throws ProductException, UserException {
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
        Long productId = addProductWithLogin(requestAddDto);
        
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
        
        assertThrows(InvalidDateException.class, () -> {
            productService.updateProduct(productId, requestUpdateDto);
        });
    }
    
    @Test
    @DisplayName("상품 목록 조회 성공")
    void getProductsSuccess() throws ProductException, UserException {
        
        addProducts();
        
        // 마감 임박순 정렬
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<ResponseProductListDto> pagedProducts = productService.getProducts("EXPIRING",
                                                                                pageRequest);
        ResponseProductListDto expiringFirst = pagedProducts.stream().collect(Collectors.toList())
            .get(0);
        assertAll("0page의 0번째 상품은 성공적으로 페이징된다",
                  () -> assertEquals(100000L,
                                     expiringFirst.getPrice()),
                  () -> assertEquals(10L, expiringFirst.getQuantity()));
        
        pageRequest = PageRequest.of(9, 10);
        pagedProducts = productService.getProducts("EXPIRING", pageRequest);
        ResponseProductListDto expiringLast = pagedProducts.stream().collect(Collectors.toList())
            .get(9);
        assertAll("9page의 9번째 상품은 성공적으로 페이징된다",
                  () -> assertEquals(1000L,
                                     expiringLast.getPrice()));
        
        // 수량 적은순 정렬
        pageRequest = PageRequest.of(0, 10);
        pagedProducts = productService.getProducts("QUANTITY_ASC", pageRequest);
        
        ResponseProductListDto quantityFirst = pagedProducts.stream().collect(Collectors.toList())
            .get(0);
        assertAll("0page의 0번째 상품은 성공적으로 페이징된다",
                  () -> assertEquals(100000L,
                                     quantityFirst.getPrice()),
                  () -> assertEquals(10L, quantityFirst.getQuantity()));
        
        pageRequest = PageRequest.of(9, 10);
        pagedProducts = productService.getProducts("QUANTITY_ASC", pageRequest);
        ResponseProductListDto quantityLast = pagedProducts.stream().collect(Collectors.toList())
            .get(9);
        assertAll("9page의 9번째 상품은 성공적으로 페이징된다",
                  () -> assertEquals(1000L,
                                     quantityLast.getPrice()),
                  () -> assertEquals(100L, quantityLast.getQuantity())
        );
    }
    
    @Test
    @DisplayName("상품 목록 조회 실패 - SortCondition")
    void getProductsFailWithSortCondition() throws ProductException, UserException {
        addProducts();
        assertThrows(UnsupportedSortConditionException.class, () -> {
            PageRequest pageRequest = PageRequest.of(0, 10);
            productService.getProducts("TEST", pageRequest);
        });
    }
    
    @Test
    @DisplayName("내 상품 목록 조회")
    void getMyProducts() throws ProductException, UserException {
        
        // given
        User seller = createUser(new RequestSignUpDto("test name", "test@test.com",
                                                      "testtest123", "ADMIN",
                                                      "01000000000",
                                                      "객체지향도 Java시 Spring동"));
        loginService.login(new RequestLoginDto(seller.getEmail(), seller.getPassword()));
        for (int i = 0; i < 11; i++) {
            RequestAddProductDto requestAddDto =
                RequestAddProductDto.builder()
                    .name("test product")
                    .price(100000L)
                    .explanation("test explanation!!\nhihihihi")
                    .quantity(10L)
                    .maximumPurchaseQuantity(10L)
                    .openDate(openDate)
                    .closeDate(closeDate)
                    .build();
            productService.addProduct(requestAddDto);
        }
        LocalDateTime now = LocalDateTime.now();
        String nowToString = now.format(DateTimeFormatter.ofPattern("yyMMdd"));
        
        // when
        Page<ResponseProductListDto> products = productService.getMyProducts(nowToString,
                                                                             nowToString,
                                                                             PageRequest.of(0, 1));
        
        // then
        Assertions.assertEquals(11, products.getTotalElements());
        Assertions.assertEquals(2, products.getTotalPages());
    }
    
    @Test
    @DisplayName("productId로 구매한 User 목록 조회")
    void getUsersByProduct() throws ProductException, OrderException, UserException {
        
        // given
        User seller = createUser(new RequestSignUpDto("seller name", "test@test.com",
                                                      "testtest123", "ADMIN",
                                                      "01000000000",
                                                      "객체지향도 Java시 Spring동"));
        loginService.login(new RequestLoginDto(seller.getEmail(), seller.getPassword()));
        RequestAddProductDto requestAddDto =
            RequestAddProductDto.builder()
                .name("test product")
                .price(100000L)
                .explanation("test explanation!!\nhihihihi")
                .quantity(1000L)
                .maximumPurchaseQuantity(10L)
                .openDate(openDate)
                .closeDate(closeDate)
                .build();
        Long productId = productService.addProduct(requestAddDto);
        for (int i = 0; i < 21; i++) {
            User buyer = createUser(new RequestSignUpDto("buyer name" + i, i + "test@test.com",
                                                         "testtest123", "USER",
                                                         "010111111" + i,
                                                         "객체지향도 Java시 Spring동"));
            loginService.login(new RequestLoginDto(buyer.getEmail(), buyer.getPassword()));
            orderService.orderProduct(RequestOrderDto.builder()
                                          .productId(productId)
                                          .receiverName("구매자")
                                          .address("구매자 주소")
                                          .receiverContact("01011111111")
                                          .quantity(5L)
                                          .build());
        }
        loginService.login(new RequestLoginDto(seller.getEmail(), seller.getPassword()));
        
        // when
        Page<ResponseUserListDto> users = productService.getUsersByProduct(productId,
                                                                           PageRequest.of(0, 10));
        
        // then
        Assertions.assertEquals(21, users.getTotalElements());
        Assertions.assertEquals(2, users.getTotalPages());
    }
    
    private void addProducts() throws ProductException, UserException {
        User seller = createUser(new RequestSignUpDto("test name", "test@test.com",
                                                      "testtest123", "ADMIN",
                                                      "01000000000",
                                                      "객체지향도 Java시 Spring동"));
        loginService.login(new RequestLoginDto(seller.getEmail(), seller.getPassword()));
        
        for (int i = 0; i < 25; i++) {
            RequestAddProductDto requestAddDto =
                RequestAddProductDto.builder()
                    .name("test product")
                    .price(100000L)
                    .explanation("test explanation!!\nhihihihi")
                    .quantity(10L)
                    .maximumPurchaseQuantity(10L)
                    .openDate(openDate)
                    .closeDate(closeDate)
                    .build();
            productService.addProduct(requestAddDto);
        }
        
        LocalDateTime closeDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
            .plusDays(1);
        for (int i = 0; i < 25; i++) {
            RequestAddProductDto requestAddDto =
                RequestAddProductDto.builder()
                    .name("test product")
                    .price(1000L)
                    .explanation("test explanation!!\nhihihihi")
                    .quantity(100L)
                    .maximumPurchaseQuantity(10L)
                    .openDate(openDate)
                    .closeDate(closeDate)
                    .build();
            productService.addProduct(requestAddDto);
        }
        
        for (int i = 10; i < 35; i++) {
            RequestAddProductDto requestAddDto =
                RequestAddProductDto.builder()
                    .name("test product")
                    .price(1000L)
                    .explanation("test explanation!!\nhihihihi")
                    .quantity(100L - i * 2)
                    .maximumPurchaseQuantity(10L)
                    .openDate(openDate)
                    .closeDate(closeDate)
                    .build();
            productService.addProduct(requestAddDto);
        }
        closeDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
            .plusHours(2);
        for (int i = 20; i < 45; i++) {
            RequestAddProductDto requestAddDto =
                RequestAddProductDto.builder()
                    .name("test product")
                    .price(700000L)
                    .explanation("test explanation!!\nhihihihi")
                    .quantity(100L - i)
                    .maximumPurchaseQuantity(10L)
                    .openDate(openDate)
                    .closeDate(closeDate)
                    .build();
            productService.addProduct(requestAddDto);
        }
    }
    
    // 회원 등록
    private User createUser(RequestSignUpDto requestDto) throws UserException {
        
        return userRepository.findById(userService.signUp(requestDto)).get();
    }
    
    // 상품 추가
    private Long addProductWithLogin(RequestAddProductDto requestDto)
        throws ProductException, UserException {
        User seller = createUser(new RequestSignUpDto("test name", "test@test.com",
                                                      "testtest123", "ADMIN",
                                                      "01000000000",
                                                      "객체지향도 Java시 Spring동"));
        loginService.login(new RequestLoginDto(seller.getEmail(), seller.getPassword()));
        Long productId = productService.addProduct(requestDto);
        
        return productId;
    }
}