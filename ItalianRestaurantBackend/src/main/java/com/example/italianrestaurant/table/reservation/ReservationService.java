package com.example.italianrestaurant.table.reservation;

import com.example.italianrestaurant.security.UserPrincipal;
import com.example.italianrestaurant.table.Table;
import com.example.italianrestaurant.table.TableService;
import com.example.italianrestaurant.user.User;
import com.example.italianrestaurant.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final TableService tableService;

    public Reservation addReservation(UserPrincipal userPrincipal, ReservationDto reservationDto){
        Table table = tableService.getTableById(reservationDto.getTableId());
        User user = userService.getUserByEmail(userPrincipal.getEmail());

        return reservationRepository.save(
                Reservation.builder()
                        .table(table)
                        .reservationDateStart(reservationDto.getReservationDateStart())
                        .reservationDateEnd(reservationDto.getReservationDateEnd())
                        .user(user)
                        .build()
        );
    }

    public List<Reservation> getReservations(UserPrincipal userPrincipal){
        return this.reservationRepository.getAllByUserEmail(userPrincipal.getEmail());
    }

    public void deleteReservation(Long id){
        if(this.reservationRepository.existsById(id)){
            this.reservationRepository.deleteById(id);
        }
        else {
            throw new EntityNotFoundException("Table with id: " + id + " doesn't exist");
        }
    }
}
