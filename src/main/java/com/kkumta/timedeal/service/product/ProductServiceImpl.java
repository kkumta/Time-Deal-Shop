package com.kkumta.timedeal.service.product;

import static org.springframework.data.domain.Sort.Order.asc;

import com.kkumta.timedeal.api.dto.product.*;
import com.kkumta.timedeal.domain.product.*;
import com.kkumta.timedeal.domain.UserRepository;
import com.kkumta.timedeal.domain.UserType;
import com.kkumta.timedeal.exception.product.*;
import com.kkumta.timedeal.exception.user.InvalidCredentialsException;
import com.kkumta.timedeal.exception.user.LoginInfoNotFoundException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Long addProduct(RequestAddProductDto requestDto) throws ProductException {
        
        Object userName = httpSession.getAttribute("NAME");
        Object userType = httpSession.getAttribute("TYPE");
        
        if (userName == null || userType == null) {
            throw new LoginInfoNotFoundException();
        }
        
        if (!userType.toString().equals("ADMIN")) {
            throw new InvalidCredentialsException("ADMIN 권한으로 로그인되지 않았습니다.");
        }
        
        if (requestDto.getCloseDate().minusMinutes(10).isBefore(requestDto.getOpenDate())) {
            throw new InvalidDateException();
        }
        ;
        
        Product product = Product.builder()
            .sellerName(userName.toString())
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
    
    @Override
    public ResponseProductDto getProductInfo(Long id) throws ProductException {
        Object userName = httpSession.getAttribute("NAME");
        Object userType = httpSession.getAttribute("TYPE");
        
        // 계정 정보 확인
        if (userName == null || userType == null) {
            throw new LoginInfoNotFoundException();
        } else if (userRepository.findByName(userName.toString()).isEmpty()) {
            throw new RuntimeException("계정 이름이 유효하지 않습니다.");
        } else {
            UserType.find(userType.toString());
        }
        
        // 상품 정보 확인
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException());
        if (product.getIsDeleted()) {
            throw new ProductDeletedException();
        }
        
        return ResponseProductDto.builder()
            .sellerName(userName.toString())
            .productName(product.getName())
            .price(product.getPrice())
            .explanation(product.getExplanation())
            .quantity(product.getQuantity())
            .maximumPurchaseQuantity(product.getMaximumPurchaseQuantity())
            .openDate(product.getOpenDate())
            .closeDate(product.getCloseDate())
            .isSellingPaused(product.getIsSellingPaused())
            .build();
    }
    
    @Override
    @Transactional
    public void deleteProduct(Long id) throws ProductException {
        String userName = httpSession.getAttribute("NAME").toString();
        
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException());
        
        if (!userName.equals(product.getSellerName())) {
            throw new SellerMismatchException();
        }
        
        if (product.getIsDeleted()) {
            throw new ProductDeletedException();
        }
        
        product.delete();
    }
    
    @Override
    @Transactional
    public ResponseProductDto updateProduct(Long productId, RequestUpdateProductDto requestDto)
        throws ProductException {
        String userName = httpSession.getAttribute("NAME").toString();
        
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException());
        
        if (!userName.equals(product.getSellerName())) {
            throw new SellerMismatchException();
        }
        
        if (product.getIsDeleted()) {
            throw new ProductDeletedException();
        }
        
        if (requestDto.getCloseDate().minusMinutes(10).isBefore(requestDto.getOpenDate())) {
            throw new InvalidDateException();
        }
        
        product.update(requestDto.getName(), requestDto.getPrice(), requestDto.getExplanation(),
                       requestDto.getQuantity(), requestDto.getMaximumPurchaseQuantity(),
                       requestDto.getOpenDate(), requestDto.getCloseDate(),
                       requestDto.getIsSellingPaused());
        
        return ResponseProductDto.builder()
            .sellerName(userName)
            .productName(product.getName())
            .price(product.getPrice())
            .explanation(product.getExplanation())
            .quantity(product.getQuantity())
            .maximumPurchaseQuantity(product.getMaximumPurchaseQuantity())
            .openDate(product.getOpenDate())
            .closeDate(product.getCloseDate())
            .isSellingPaused(product.getIsSellingPaused())
            .build();
    }
    
    @Override
    public Page<ResponseProductListDto> getProducts(String sortCondition, Pageable pageable)
        throws ProductException {
        Object userName = httpSession.getAttribute("NAME");
        Object userType = httpSession.getAttribute("TYPE");
        
        // 계정 정보 확인
        if (userName == null || userType == null) {
            throw new LoginInfoNotFoundException();
        } else if (userRepository.findByName(userName.toString()).isEmpty()) {
            throw new RuntimeException("계정 이름이 유효하지 않습니다.");
        } else {
            UserType.find(userType.toString());
        }
        
        SortCondition.find(sortCondition);
        
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Page<Product> products;
        PageRequest pageRequest = null;
        if (sortCondition.equals(SortCondition.EXPIRING.name())) {
            pageRequest = PageRequest.of(pageable.getPageNumber(), 10,
                                         Sort.by(asc("closeDate")));
        } else if (sortCondition.equals(SortCondition.QUANTITY_ASC.name())) {
            pageRequest = PageRequest.of(pageable.getPageNumber(), 10,
                                         Sort.by(asc("quantity")));
        }
        products = productRepository.findAllByIsDeletedFalseAndIsSellingPausedFalseAndQuantityIsGreaterThanAndOpenDateLessThanEqualAndCloseDateAfter(
            0L, now, now, pageRequest);
        return products.map(ResponseProductListDto::of);
    }
}