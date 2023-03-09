package com.kkumta.timedeal.service.user;

import com.kkumta.timedeal.domain.User;
import com.kkumta.timedeal.domain.UserRepository;
import com.kkumta.timedeal.api.dto.user.RequestSignUpDto;
import com.kkumta.timedeal.domain.UserType;
import com.kkumta.timedeal.exception.user.ValidateUniqueEmailException;
import com.kkumta.timedeal.exception.user.ValidateUniqueNameException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    
    public Boolean validateUniqueName(String name) {
        
        if (userRepository.findByName(name).isEmpty()) {
            return true;
        }
        return false;
    }
    
    public Boolean validateUniqueEmail(String email) {
        
        if (userRepository.findByEmail(email).isEmpty()) {
            return true;
        }
        return false;
    }
    
    @Transactional
    public Long signUp(RequestSignUpDto requestSignUpDto) {
        
        if (!validateUniqueName(requestSignUpDto.getName())) {
            throw new ValidateUniqueNameException();
        }
        if (!validateUniqueEmail(requestSignUpDto.getEmail())) {
            throw new ValidateUniqueEmailException();
        }
        UserType.find(requestSignUpDto.getType());
        
        User user = User.builder()
            .name(requestSignUpDto.getName())
            .email(requestSignUpDto.getEmail())
            .password(requestSignUpDto.getPassword())
            .type(UserType.valueOf(requestSignUpDto.getType()))
            .phoneNumber(requestSignUpDto.getPhoneNumber())
            .address(requestSignUpDto.getAddress())
            .build();
        
        Long userId = userRepository.save(user).getId();
        
        return userId;
    }
}