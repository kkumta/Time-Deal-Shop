package com.kkumta.timedeal.api;

import com.kkumta.timedeal.api.dto.user.RequestSignUpDto;
import com.kkumta.timedeal.api.dto.user.ResponseUserDto;
import com.kkumta.timedeal.exception.user.UserException;
import com.kkumta.timedeal.service.user.UserService;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("users")
@RestController
public class UserController {
    
    private final UserService userService;
    
    @GetMapping("/name-validation/{name}")
    public ResponseEntity<Boolean> validateUniqueName(@PathVariable String name) {
        return ResponseEntity.ok(userService.validateUniqueName(name));
    }
    
    @GetMapping("/email-validation/{email}")
    public ResponseEntity<Boolean> validateUniqueEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.validateUniqueEmail(email));
    }
    
    @PostMapping
    public ResponseEntity<Long> signUp(@Valid @RequestBody RequestSignUpDto requestSignUpDto)
        throws UserException {
        Long userId = userService.signUp(requestSignUpDto);
        return ResponseEntity.created(URI.create("users/" + userId)).body(userId);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) throws UserException {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ResponseUserDto> getUserInfo(@PathVariable Long id) throws UserException  {
        return ResponseEntity.ok(userService.getUserInfo(id));
    }
}