package com.example.demo.src.payment.entity;

import com.example.demo.src.user.entity.User;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "BILLING_KEY")
public class BillingKey {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="email")
    private User user;

    @Column(length = 100)
    private String billingKey;

    @Builder
    public BillingKey(UUID id, User user, String billingKey) {
        this.id = id;
        this.user = user;
        this.billingKey = billingKey;
    }

    public void updateUser(User user){
        this.user = user;
    }

    public void updateBillingKey(String billingKey){
        this.billingKey = billingKey;
    }
}
