package com.davidantas.stayra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "property_photo", uniqueConstraints =
        @UniqueConstraint(name = "uk_property_photo_position", columnNames = {"property_id", "display_order"}))
@Getter
@NoArgsConstructor
public class PropertyPhoto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @NotBlank @Size(max = 2048)
    @Column(nullable = false, length = 2048)
    private String url;

    @Size(max = 255)
    @Column(name = "alt_text", length = 255)
    private String altText;

    @NotNull @PositiveOrZero
    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;
}
