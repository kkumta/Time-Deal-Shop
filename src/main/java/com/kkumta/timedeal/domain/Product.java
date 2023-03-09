package com.kkumta.timedeal.domain;

import static javax.persistence.GenerationType.AUTO;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Entity
public class Product extends BaseTimeEntity {
    
    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = AUTO)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User seller;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private Long price;
    
    @Column(nullable = false)
    private String explanation;
    
    @Column(nullable = false)
    private Long quantity;
    
    @Column(nullable = false)
    private Long maximumPurchaseQuantity;
    
    @Column(nullable = false)
    private LocalDateTime openDate;
    
    @Column(nullable = false)
    private LocalDateTime closeDate;
    
    @Column(nullable = false)
    private Boolean isSellingPaused;
    
    @Column(nullable = false)
    private Boolean isDeleted;
    
    @Builder
    public Product(User seller, String name, Long price, String explanation, Long quantity,
                   Long maximumPurchaseQuantity, LocalDateTime openDate, LocalDateTime closeDate) {
        this.seller = seller;
        this.name = name;
        this.price = price;
        this.explanation = explanation;
        this.quantity = quantity;
        this.maximumPurchaseQuantity = maximumPurchaseQuantity;
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.isSellingPaused = false;
        this.isDeleted = false;
    }
    
    public void update(String name, Long price, String explanation, Long quantity,
                       Long maximumPurchaseQuantity, LocalDateTime openDate,
                       LocalDateTime closeDate, Boolean isSellingPaused, Boolean isDeleted) {
        this.name = name;
        this.price = price;
        this.explanation = explanation;
        this.quantity = quantity;
        this.maximumPurchaseQuantity = maximumPurchaseQuantity;
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.isSellingPaused = isSellingPaused;
        this.isDeleted = isDeleted;
    }
}
