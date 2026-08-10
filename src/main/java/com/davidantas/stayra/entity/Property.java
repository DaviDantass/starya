package com.davidantas.stayra.entity;

import com.davidantas.stayra.entity.enums.PropertyType;
import com.davidantas.stayra.entity.enums.Status;
import jakarta.persistence.*;

@Entity
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String address;

    private String pricePerNight;

    private PropertyType type;

    @ManyToOne
    @JoinColumn(name = "host_id")
    private User host;

    private Status status;
}
