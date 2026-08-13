package com.davidantas.stayra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "daily_price", uniqueConstraints =
        @UniqueConstraint(name = "uk_daily_price_property_date", columnNames = {"property_id", "price_date"}))
@Getter
@NoArgsConstructor
public class DailyPrice {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @NotNull
    @Column(name = "price_date", nullable = false)
    private LocalDate date;

    @NotNull @DecimalMin("0.01")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;
}
