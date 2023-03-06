package com.kkumta.timedeal.service.user;

import com.kkumta.timedeal.api.dto.user.RequestLoginDto;
import com.kkumta.timedeal.domain.User;
import com.kkumta.timedeal.domain.UserRepository;
import com.kkumta.timedeal.exception.user.InvalidCredentialsException;
import com.kkumta.timedeal.exception.user.LoginInfoNotFoundException;
import com.kkumta.timedeal.exception.user.UserNotFoundException;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LoginServiceImpl implements LoginService {
    
    private final UserRepository userRepository;
    private final HttpSession httpSession;
    
    public void login(RequestLoginDto requestDto) {
        
        // DB에서 로그인 정보와 일치하는 유저를 찾는다
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();
        Optional<User> user = userRepository.findByEmail(email);
        
        // DB에 로그인 이메일을 가진 유저가 없을 경우
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        
        // 이메일에 해당되는 비밀번호가 입력되지 않았을 경우
        if (!user.get().getPassword().equals(password)) {
            throw new InvalidCredentialsException();
        }
        
        // 로그인 정보가 정상적일 경우 로그인을 한다
        httpSession.setAttribute("NAME", user.get().getName());
        httpSession.setAttribute("TYPE", user.get().getUserType());
    }
    
    public void logout() {
        
        // 세션에 로그인 정보가 없을 경우
        if (httpSession.getAttribute("NAME") == null || httpSession.getAttribute("TYPE") == null) {
            throw new LoginInfoNotFoundException();
        }
        
        // 로그아웃을 한다
        httpSession.removeAttribute("NAME");
        httpSession.removeAttribute("TYPE");
    }
}