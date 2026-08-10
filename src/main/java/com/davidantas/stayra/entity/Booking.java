package com.davidantas.stayra.entity;

import com.davidantas.stayra.entity.enums.BookingStatus;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Many -> Booking, 1 -> Guest
    @JoinColumn(name = "guest_id", nullable = false)
    private User guest;

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    private LocalDate checkIn;

    private LocalDate checkOut;

    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
