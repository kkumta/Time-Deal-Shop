package com.kkumta.timedeal.domain.order;

import static javax.persistence.GenerationType.*;

import com.kkumta.timedeal.domain.BaseTimeEntity;
import com.kkumta.timedeal.domain.product.Product;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "orders")
@Entity
public class Order extends BaseTimeEntity {
    
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = AUTO)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    
    private Long userId;
    
    @Embedded
    private DeliveryInfo deliveryInfo;
    
    private Long quantity;
    
    private Long amountPrice;
    
    @Builder
    public Order(Product product, Long userId, DeliveryInfo deliveryInfo, Long quantity,
                 Long amountPrice) {
        this.product = product;
        this.userId = userId;
        this.deliveryInfo = deliveryInfo;
        this.quantity = quantity;
        this.amountPrice = amountPrice;
    }
    
    public static Order createOrder(Product product, Long userId, DeliveryInfo deliveryInfo,
                                    Long quantity) {
        Order order = new Order();
        order.product = product;
        order.userId = userId;
        order.deliveryInfo = deliveryInfo;
        order.quantity = quantity;
        order.amountPrice = quantity * product.getPrice();
        product.deleteStock(quantity);
        
        return order;
    }
}
