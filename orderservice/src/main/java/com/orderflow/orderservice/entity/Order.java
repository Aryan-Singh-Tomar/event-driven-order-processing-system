package com.orderflow.orderservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data                    // (1) getter + setter + equals + hashCode + toString
@NoArgsConstructor       // (2) Hibernate ke liye zaroori — empty constructor
@AllArgsConstructor      // (3) sab fields ka constructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "customer_name", nullable = false)    // (5)
    private String customerName;
    @Column(name = "product_name", nullable = false)
    private String productName;
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    @Enumerated(EnumType.STRING)                         // (7)
    @Column(nullable = false)
    private OrderStatus status;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist                                          // (8)
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = OrderStatus.CREATED;
        }
    }

}
