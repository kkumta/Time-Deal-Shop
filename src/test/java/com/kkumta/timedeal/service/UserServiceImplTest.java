package com.kkumta.timedeal.service;

import com.kkumta.timedeal.domain.User;
import com.kkumta.timedeal.domain.UserRepository;
import com.kkumta.timedeal.api.dto.user.RequestSignUpDto;
import com.kkumta.timedeal.service.user.UserService;
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
    private UserRepository userRepository;
    
    @AfterEach
    void clear() {
        userRepository.deleteAll();
    }
    
    @Test
    @DisplayName("중복 name 검증")
    void validateUniqueName() {
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
    void validateUniqueEmail() {
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
    void signUpSuccess() {
        RequestSignUpDto requestSignUpDto = new RequestSignUpDto("test name", "test@test.com",
                                                                 "testtest123", "ADMIN",
                                                                 "01000000000",
                                                                 "객체지향도 Java시 Spring동");
        User user = userRepository.findById(userService.signUp(requestSignUpDto)).get();
        Assertions.assertEquals("test name", user.getName());
    }
}