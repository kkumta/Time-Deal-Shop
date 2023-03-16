package com.kkumta.timedeal.service.user;

import com.kkumta.timedeal.api.dto.user.RequestSignUpDto;
import com.kkumta.timedeal.api.dto.user.ResponseUserDto;
import com.kkumta.timedeal.exception.user.UserException;
import com.kkumta.timedeal.exception.user.UserNotFoundException;

public interface UserService {
    
    Boolean validateUniqueName(String name);
    
    Boolean validateUniqueEmail(String email);
    
    Long signUp(RequestSignUpDto requestSignUpDto) throws UserException;
    
    void deleteUser(Long id) throws UserException;
    
    ResponseUserDto getUserInfo(Long id) throws UserException;
}
