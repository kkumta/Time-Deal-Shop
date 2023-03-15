package com.kkumta.timedeal.service.user;

import com.kkumta.timedeal.api.dto.user.RequestLoginDto;
import com.kkumta.timedeal.exception.user.LoginInfoNotFoundException;
import com.kkumta.timedeal.exception.user.UserException;
import com.kkumta.timedeal.exception.user.UserNotFoundException;

public interface LoginService {
    
    void login(RequestLoginDto requestDto) throws UserNotFoundException, UserException;
    
    void logout() throws LoginInfoNotFoundException, UserException;
}
