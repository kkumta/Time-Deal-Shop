package com.kkumta.timedeal.service;

import com.kkumta.timedeal.api.dto.user.RequestLoginDto;
import com.kkumta.timedeal.api.dto.user.RequestSignUpDto;
import com.kkumta.timedeal.domain.UserRepository;
import com.kkumta.timedeal.exception.user.InvalidCredentialsException;
import com.kkumta.timedeal.exception.user.LoginInfoNotFoundException;
import com.kkumta.timedeal.exception.user.UserNotFoundException;
import com.kkumta.timedeal.service.user.LoginService;
import com.kkumta.timedeal.service.user.UserService;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LoginServiceImplTest {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private LoginService loginService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private HttpSession httpSession;
    
    @AfterEach
    void clear() {
        userRepository.deleteAll();
    }
    
    @Test
    @DisplayName("로그인_성공")
    void loginSuccess() {
        RequestSignUpDto requestSignUpDto = new RequestSignUpDto("test name", "test@test.com",
                                                                 "testtest123", "admin",
                                                                 "01000000000",
                                                                 "객체지향도 Java시 Spring동");
        userService.signUp(requestSignUpDto);
        RequestLoginDto requestDto = new RequestLoginDto("test@test.com", "testtest123");
        loginService.login(requestDto);
        Assertions.assertEquals(requestSignUpDto.getName(), httpSession.getAttribute("NAME"));
        Assertions.assertEquals(requestSignUpDto.getUserType(), httpSession.getAttribute("TYPE"));
    }
    
    @Test
    @DisplayName("로그인_실패_없는_ID")
    void loginFailWithId() {
        RequestSignUpDto requestSignUpDto = new RequestSignUpDto("test name", "test@test.com",
                                                                 "testtest123", "admin",
                                                                 "01000000000",
                                                                 "객체지향도 Java시 Spring동");
        userService.signUp(requestSignUpDto);
        RequestLoginDto requestDto = new RequestLoginDto("fail@test.com", "testtest123");
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            loginService.login(requestDto);
        });
    }
    
    @Test
    @DisplayName("로그인_실패_틀린_비밀번호")
    void loginFailWithPassword() {
        RequestSignUpDto requestSignUpDto = new RequestSignUpDto("test name", "test@test.com",
                                                                 "testtest123", "admin",
                                                                 "01000000000",
                                                                 "객체지향도 Java시 Spring동");
        userService.signUp(requestSignUpDto);
        RequestLoginDto requestDto = new RequestLoginDto("test@test.com", "failfail123");
        Assertions.assertThrows(InvalidCredentialsException.class, () -> {
            loginService.login(requestDto);
        });
    }
    
    @Test
    @DisplayName("로그아웃_성공")
    void logoutSuccess() {
        RequestSignUpDto requestSignUpDto = new RequestSignUpDto("test name", "test@test.com",
                                                                 "testtest123", "admin",
                                                                 "01000000000",
                                                                 "객체지향도 Java시 Spring동");
        userService.signUp(requestSignUpDto);
        RequestLoginDto requestDto = new RequestLoginDto("test@test.com", "testtest123");
        loginService.login(requestDto);
        loginService.logout();
        Assertions.assertEquals(null, httpSession.getAttribute("NAME"));
        Assertions.assertEquals(null, httpSession.getAttribute("TYPE"));
    }
    
    @Test
    @DisplayName("로그아웃_실패_세션에_NAME_없음")
    void logoutFailWithName() {
        RequestSignUpDto requestSignUpDto = new RequestSignUpDto("test name", "test@test.com",
                                                                 "testtest123", "admin",
                                                                 "01000000000",
                                                                 "객체지향도 Java시 Spring동");
        userService.signUp(requestSignUpDto);
        RequestLoginDto requestDto = new RequestLoginDto("test@test.com", "testtest123");
        loginService.login(requestDto);
        httpSession.removeAttribute("NAME");
        Assertions.assertThrows(LoginInfoNotFoundException.class, () -> {
            loginService.logout();
        });
    }
    
    @Test
    @DisplayName("로그아웃_실패_세션에_TYPE_없음")
    void logoutFailWithType() {
        RequestSignUpDto requestSignUpDto = new RequestSignUpDto("test name", "test@test.com",
                                                                 "testtest123", "admin",
                                                                 "01000000000",
                                                                 "객체지향도 Java시 Spring동");
        userService.signUp(requestSignUpDto);
        RequestLoginDto requestDto = new RequestLoginDto("test@test.com", "testtest123");
        loginService.login(requestDto);
        httpSession.removeAttribute("TYPE");
        Assertions.assertThrows(LoginInfoNotFoundException.class, () -> {
            loginService.logout();
        });
    }
}