package com.kkumta.timedeal.service.user;

import com.kkumta.timedeal.domain.User;
import com.kkumta.timedeal.domain.UserRepository;
import com.kkumta.timedeal.api.dto.user.RequestSignUpDto;
import com.kkumta.timedeal.domain.UserType;
import com.kkumta.timedeal.domain.product.Product;
import com.kkumta.timedeal.domain.product.ProductRepository;
import com.kkumta.timedeal.exception.user.InvalidCredentialsException;
import com.kkumta.timedeal.exception.user.LoginInfoNotFoundException;
import com.kkumta.timedeal.exception.user.UserException;
import com.kkumta.timedeal.exception.user.ValidateUniqueEmailException;
import com.kkumta.timedeal.exception.user.ValidateUniqueNameException;
import java.util.List;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    
    private final HttpSession session;
    
    @Override
    public Boolean validateUniqueName(String name) {
        
        if (userRepository.findByName(name).isEmpty()) {
            return true;
        }
        return false;
    }
    
    @Override
    public Boolean validateUniqueEmail(String email) {
        
        if (userRepository.findByEmail(email).isEmpty()) {
            return true;
        }
        return false;
    }
    
    @Override
    @Transactional
    public Long signUp(RequestSignUpDto requestSignUpDto) throws UserException {
        
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
    
    @Override
    @Transactional
    public void deleteUser(Long id) throws UserException {
        
        Object userName = session.getAttribute("NAME");
        Object userType = session.getAttribute("TYPE");
        User user = userRepository.findByName(userName.toString()).get();
        
        // 계정 정보 확인
        if (userName == null || userType == null) {
            throw new LoginInfoNotFoundException();
        } else if (id.longValue() != user.getId().longValue()) {
            throw new InvalidCredentialsException("로그인한 User와 탈퇴하려는 User가 다릅니다.");
        }
        
        if (userType.equals("ADMIN")) {
            List<Product> products = productRepository.findAllBySellerName(
                userName.toString());
            for (Product product : products) {
                product.delete();
            }
        }
        
        userRepository.delete(user);
    }
}