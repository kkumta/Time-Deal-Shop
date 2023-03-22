package com.kkumta.timedeal.domain.product;

import static javax.persistence.GenerationType.AUTO;

import com.kkumta.timedeal.domain.BaseTimeEntity;
import com.kkumta.timedeal.domain.order.Order;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(indexes = @Index(name = "index_close", columnList = "closeDate"))
@Entity
public class Product extends BaseTimeEntity {
    
    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = AUTO)
    private Long id;
    
    @Column(nullable = false)
    private String sellerName;
    
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
    private Boolean isSellingPaused = false;
    
    @Column(nullable = false)
    private Boolean isDeleted = false;
    
    @OneToMany(mappedBy = "product")
    private List<Order> orders = new ArrayList<>();
    
    @Builder
    public Product(String sellerName, String name, Long price, String explanation, Long quantity,
                   Long maximumPurchaseQuantity, LocalDateTime openDate, LocalDateTime closeDate) {
        this.sellerName = sellerName;
        this.name = name;
        this.price = price;
        this.explanation = explanation;
        this.quantity = quantity;
        this.maximumPurchaseQuantity = maximumPurchaseQuantity;
        this.openDate = openDate;
        this.closeDate = closeDate;
    }
    
    public void update(String name, Long price, String explanation, Long quantity,
                       Long maximumPurchaseQuantity, LocalDateTime openDate,
                       LocalDateTime closeDate, Boolean isSellingPaused) {
        this.name = name;
        this.price = price;
        this.explanation = explanation;
        this.quantity = quantity;
        this.maximumPurchaseQuantity = maximumPurchaseQuantity;
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.isSellingPaused = isSellingPaused;
    }
    
    public void delete() {
        this.isDeleted = true;
    }
    
    public void deleteStock(Long quantity) {
        this.quantity -= quantity;
    }
}
