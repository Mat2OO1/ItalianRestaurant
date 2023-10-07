package com.example.italianrestaurant.table;

import jakarta.persistence.*;
import lombok.*;

@Entity
@jakarta.persistence.Table(name = "tables")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Table {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private int number;

    @Column(name = "seats_nr", nullable = false)
    private int seats;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TableStatus status;
}
