package com.example.demo.src.user.entity;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.report.entity.Report;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@Entity
@Table(name = "FOLLOW")
public class Follow extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="followee_id", nullable = false)
    private User followee;

    @ManyToOne
    @JoinColumn(name="follower_id", nullable = false)
    private User follower;

}
