package com.kkumta.timedeal.service.user;

import com.kkumta.timedeal.api.dto.user.RequestLoginDto;

public interface LoginService {
    
    void login(RequestLoginDto requestDto);
    
    void logout();
}
