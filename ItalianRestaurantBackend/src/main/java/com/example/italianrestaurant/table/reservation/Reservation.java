package com.example.italianrestaurant.table.reservation;

import com.example.italianrestaurant.table.Table;
import com.example.italianrestaurant.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@jakarta.persistence.Table(name = "reservations")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @ToString.Exclude
    private User user;

    @OneToOne
    private Table table;

    private LocalDateTime reservationDateStart;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id) && Objects.equals(user, that.user) && Objects.equals(table, that.table) && Objects.equals(reservationDateStart, that.reservationDateStart);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, table, reservationDateStart);
    }
}
