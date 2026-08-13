package com.davidantas.stayra.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@RedisHash("booking-hold")
@Getter
@NoArgsConstructor
public class BookingHold {
    @Id
    private String id;

    @NotNull private Long propertyId;
    @NotNull private Long guestId;
    @NotNull private LocalDate checkIn;
    @NotNull private LocalDate checkOut;
    @NotNull private OffsetDateTime createdAt;

    @TimeToLive
    private Long ttlSeconds;
}
