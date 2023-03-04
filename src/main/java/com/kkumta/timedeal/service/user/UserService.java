package com.kkumta.timedeal.service.user;

import com.kkumta.timedeal.api.dto.user.RequestSignUpDto;

public interface UserService {
    
    Boolean validateUniqueName(String name);
    
    Boolean validateUniqueEmail(String email);
    
    Long signUp(RequestSignUpDto requestSignUpDto);
}
