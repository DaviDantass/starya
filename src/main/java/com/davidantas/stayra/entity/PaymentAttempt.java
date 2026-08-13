package com.davidantas.stayra.entity;

import com.davidantas.stayra.entity.enums.PaymentAttemptStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "payment_attempt", indexes = {
        @Index(name = "idx_payment_attempt_payment", columnList = "payment_id")
})
public class PaymentAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PaymentAttemptStatus status;

    @Size(max = 255)
    @Column(name = "provider_attempt_id")
    private String providerAttemptId;

    @Size(max = 100)
    @Column(name = "failure_code", length = 100)
    private String failureCode;

    @Size(max = 500)
    @Column(name = "failure_message", length = 500)
    private String failureMessage;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}
