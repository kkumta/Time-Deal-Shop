package com.kkumta.timedeal.service.user;

import com.kkumta.timedeal.api.dto.product.RequestAddProductDto;
import com.kkumta.timedeal.api.dto.user.RequestLoginDto;
import com.kkumta.timedeal.api.dto.user.ResponseUserDto;
import com.kkumta.timedeal.domain.User;
import com.kkumta.timedeal.domain.UserRepository;
import com.kkumta.timedeal.api.dto.user.RequestSignUpDto;
import com.kkumta.timedeal.exception.product.ProductException;
import com.kkumta.timedeal.exception.user.UserException;
import com.kkumta.timedeal.service.product.ProductService;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.TransactionSystemException;

@SpringBootTest
class UserServiceImplTest {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private LoginService loginService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private HttpSession session;
    
    final LocalDateTime openDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    final LocalDateTime closeDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        .plusMinutes(10);
    
    @AfterEach
    void clear() {
        userRepository.deleteAll();
    }
    
    @Test
    @DisplayName("중복 name 검증")
    void validateUniqueName() throws UserException {
        RequestSignUpDto requestSignUpDto = new RequestSignUpDto("test name", "test@test.com",
                                                                 "testtest123", "ADMIN",
                                                                 "01000000000",
                                                                 "객체지향도 Java시 Spring동");
        userService.signUp(requestSignUpDto);
        Assertions.assertEquals(false, userService.validateUniqueName("test name"));
        
        Assertions.assertEquals(true, userService.validateUniqueName("test name 2"));
    }
    
    @Test
    @DisplayName("중복 email 검증")
    void validateUniqueEmail() throws UserException {
        RequestSignUpDto requestSignUpDto = new RequestSignUpDto("test name", "test@test.com",
                                                                 "testtest123", "ADMIN",
                                                                 "01000000000",
                                                                 "객체지향도 Java시 Spring동");
        userService.signUp(requestSignUpDto);
        Assertions.assertEquals(false, userService.validateUniqueEmail("test@test.com"));
        
        Assertions.assertEquals(true, userService.validateUniqueEmail("test2@test.com"));
    }
    
    @Test
    @DisplayName("회원가입_실패")
    void signUpFail() {
        // name 길이
        RequestSignUpDto requestDto1 = new RequestSignUpDto("n", "test@test.com",
                                                            "testtest123", "ADMIN",
                                                            "01000000000", "객체지향도 Java시 Spring동");
        Assertions.assertThrows(TransactionSystemException.class, () -> {
            userService.signUp(requestDto1);
        });
        
        RequestSignUpDto requestDto2 = new RequestSignUpDto("namenamenamenamenamen",
                                                            "test@test.com",
                                                            "testtest123", "ADMIN",
                                                            "01000000000", "객체지향도 Java시 Spring동");
        Assertions.assertThrows(TransactionSystemException.class, () -> {
            userService.signUp(requestDto2);
        });
        
        // email 길이
        RequestSignUpDto requestDto3 = new RequestSignUpDto("name", "t@c.",
                                                            "testtest123", "ADMIN",
                                                            "01000000000", "객체지향도 Java시 Spring동");
        Assertions.assertThrows(TransactionSystemException.class, () -> {
            userService.signUp(requestDto3);
        });
        
        RequestSignUpDto requestDto4 = new RequestSignUpDto("name",
                                                            "testtesttesttesttesttesttesttesttesttest@testtest.com",
                                                            "testtest123", "ADMIN",
                                                            "01000000000", "객체지향도 Java시 Spring동");
        Assertions.assertThrows(TransactionSystemException.class, () -> {
            userService.signUp(requestDto4);
        });
        
        // password 길이
        RequestSignUpDto requestDto5 = new RequestSignUpDto("name", "test@test.com",
                                                            "pass", "ADMIN",
                                                            "01000000000", "객체지향도 Java시 Spring동");
        Assertions.assertThrows(TransactionSystemException.class, () -> {
            userService.signUp(requestDto5);
        });
        
        RequestSignUpDto requestDto6 = new RequestSignUpDto("name", "test@test.com",
                                                            "passwordpasswordpasswordpasswordpasswordpasswordpassword",
                                                            "ADMIN",
                                                            "01000000000", "객체지향도 Java시 Spring동");
        Assertions.assertThrows(TransactionSystemException.class, () -> {
            userService.signUp(requestDto6);
        });
    }
    
    @Test
    @DisplayName("회원가입_성공")
    void signUpSuccess() throws UserException {
        RequestSignUpDto requestSignUpDto = new RequestSignUpDto("test name", "test@test.com",
                                                                 "testtest123", "ADMIN",
                                                                 "01000000000",
                                                                 "객체지향도 Java시 Spring동");
        User user = userRepository.findById(userService.signUp(requestSignUpDto)).get();
        Assertions.assertEquals("test name", user.getName());
    }
    
    @Test
    @DisplayName("회원탈퇴")
    void deleteUser() throws UserException, ProductException {
        // given
        RequestSignUpDto requestSignUpDto = new RequestSignUpDto("test name", "test@test.com",
                                                                 "testtest123", "ADMIN",
                                                                 "01000000000",
                                                                 "객체지향도 Java시 Spring동");
        User user = userRepository.findById(userService.signUp(requestSignUpDto)).get();
        loginService.login(new RequestLoginDto(user.getEmail(), user.getPassword()));
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
        for (int i = 0; i < 2; i++) {
            productService.addProduct(requestAddDto);
        }
        
        // when
        userService.deleteUser(user.getId());
        
        // then
        Optional<User> findUser = userRepository.findById(user.getId());
        Assertions.assertEquals(Optional.empty(), findUser);
    }
    
    @Test
    @DisplayName("회원 정보 조회")
    void getUserInfo() throws UserException {
        // given
        RequestSignUpDto requestSignUpDto = new RequestSignUpDto("test name", "test@test.com",
                                                                 "testtest123", "ADMIN",
                                                                 "01000000000",
                                                                 "객체지향도 Java시 Spring동");
        User user = userRepository.findById(userService.signUp(requestSignUpDto)).get();
        loginService.login(new RequestLoginDto(user.getEmail(), user.getPassword()));
    
        // when
        ResponseUserDto responseDto = userService.getUserInfo(user.getId());
    
        // then
        Assertions.assertEquals(requestSignUpDto.getName(), responseDto.getName());
        Assertions.assertEquals(requestSignUpDto.getEmail(), responseDto.getEmail());
        Assertions.assertEquals(requestSignUpDto.getType(), responseDto.getType().name());
        Assertions.assertEquals(requestSignUpDto.getPhoneNumber(), responseDto.getPhoneNumber());
        Assertions.assertEquals(requestSignUpDto.getAddress(), responseDto.getAddress());
    }
}