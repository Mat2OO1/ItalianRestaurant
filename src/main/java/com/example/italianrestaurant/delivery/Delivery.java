package com.example.italianrestaurant.delivery;

import jakarta.persistence.*;

@Entity
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String city;

    @Column
    private String postalCode;

    @Column
    private String floor;

    @Column
    private String info;

    @Enumerated
    private DeliveryOptions deliveryOptions;
}
