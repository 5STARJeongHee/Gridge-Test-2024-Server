package com.example.demo.src.payment.entity;

import com.example.demo.common.State;
import com.example.demo.src.payment.OrderStatus;
import com.example.demo.src.payment.PayMethod;
import com.example.demo.src.user.entity.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "ORDERS")
public class Orders {

    @Id // PK를 의미하는 어노테이션
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID merchantUid;

    @Column(length = 100, nullable = false)
    private String pgCode;

    @Enumerated(EnumType.STRING)
    @Column(length = 100, nullable = false)
    private PayMethod payMethod;
    @Column(nullable = false)
    private long productId;
    
    @Column(nullable = false)
    private long amount;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="email")
    private User user;
    @Column
    private String customizerId;
    @Enumerated(EnumType.STRING)
    @Column(name = "orderStatus", nullable = false, length = 30)
    private OrderStatus orderStatus;
    @Column
    private String impId;

    @CreationTimestamp
    @Column(name = "createdAt", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 10)
    protected State state = State.ACTIVE;

    @Builder
    public Orders(Long id, UUID merchantUid, String pgCode, PayMethod payMethod, long productId, long amount, User user, String customizerId, OrderStatus orderStatus, String impId, LocalDateTime createdAt, State state) {
        this.id = id;
        this.merchantUid = merchantUid;
        this.pgCode = pgCode;
        this.payMethod = payMethod;
        this.productId = productId;
        this.amount = amount;
        this.user = user;
        this.customizerId = customizerId;
        this.orderStatus = orderStatus;
        this.impId = impId;
        this.createdAt = createdAt;
        this.state = state;
    }


    public void updateUser(User user){
        this.user = user;
    }

    public void updateOrderStatus(OrderStatus status){
        this.orderStatus = status;
    }
}
