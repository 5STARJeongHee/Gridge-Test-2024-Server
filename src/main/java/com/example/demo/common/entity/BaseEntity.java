package com.example.demo.common.entity;

import com.example.demo.common.State;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;


@Getter
@MappedSuperclass
public class BaseEntity {

    @CreationTimestamp
    @Column(name = "createdAt", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updatedAt", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 10)
    protected State state = State.ACTIVE;


}
