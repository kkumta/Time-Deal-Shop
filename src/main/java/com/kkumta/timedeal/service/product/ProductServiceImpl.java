package com.kkumta.timedeal.service.product;

import com.kkumta.timedeal.api.dto.product.RequestAddProductDto;
import com.kkumta.timedeal.domain.Product;
import com.kkumta.timedeal.domain.ProductRepository;
import com.kkumta.timedeal.domain.User;
import com.kkumta.timedeal.domain.UserRepository;
import com.kkumta.timedeal.exception.user.InvalidCredentialsException;
import com.kkumta.timedeal.exception.user.LoginInfoNotFoundException;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProductServiceImpl implements ProductService {
    
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final HttpSession httpSession;
    
    @Override
    @Transactional
    public Long addProduct(RequestAddProductDto requestDto) {
        
        Object userName = httpSession.getAttribute("NAME");
        Object userType = httpSession.getAttribute("TYPE");
        
        if (userName == null || userType == null) {
            throw new LoginInfoNotFoundException();
        }
        
        if (!userType.toString().equals("ADMIN")) {
            throw new InvalidCredentialsException("ADMIN 권한으로 로그인되지 않았습니다.");
        }
        
        if (requestDto.getCloseDate().minusMinutes(10).isBefore(requestDto.getOpenDate())) {
            throw new RuntimeException(
                "CloseDate는 OpenDate보다 10분 이상 뒤여야 합니다. 즉, 상품이 오픈된 상태로 10분 이상 유지될 수 있어야 합니다.");
        }
        
        User seller = userRepository.findByName(userName.toString())
            .orElseThrow(() -> new RuntimeException("계정 정보가 유효하지 않습니다."));
        
        Product product = Product.builder()
            .seller(seller)
            .name(requestDto.getName())
            .price(requestDto.getPrice())
            .explanation(requestDto.getExplanation())
            .quantity(requestDto.getQuantity())
            .maximumPurchaseQuantity(requestDto.getMaximumPurchaseQuantity())
            .openDate(requestDto.getOpenDate())
            .closeDate(requestDto.getCloseDate())
            .build();
        
        return productRepository.save(product).getId();
    }
}
