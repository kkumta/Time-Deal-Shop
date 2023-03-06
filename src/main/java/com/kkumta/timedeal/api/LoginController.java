package com.kkumta.timedeal.api;

import com.kkumta.timedeal.api.dto.user.RequestLoginDto;
import com.kkumta.timedeal.service.user.LoginService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping
@RestController
public class LoginController {
    
    private final LoginService loginService;
    
    @PostMapping("/login")
    public void login(@Valid @RequestBody RequestLoginDto requestDto) {
        loginService.login(requestDto);
    }
    
    @DeleteMapping("/logout")
    public void logout() {
        loginService.logout();
    }
}