package com.example.italianrestaurant.table.reservation;

import com.example.italianrestaurant.security.UserPrincipal;
import com.example.italianrestaurant.table.Table;
import com.example.italianrestaurant.table.TableService;
import com.example.italianrestaurant.user.User;
import com.example.italianrestaurant.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final TableService tableService;
    private final ModelMapper modelMapper;

    public Reservation addReservation(UserPrincipal userPrincipal, ReservationDto reservationDto){
        Table table = tableService.getTableById(reservationDto.getTableId());
        User user = userService.getUserByEmail(userPrincipal.getEmail());
        Reservation reservation = modelMapper.map(reservationDto, Reservation.class);
        reservation.setTable(table);
        reservation.setUser(user);
        return reservationRepository.save(reservation);
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

    public List<Table> getReservedTables(){
        LocalDateTime timeFrom = LocalDateTime.now().minusHours(1);
        LocalDateTime timeTo = LocalDateTime.now().plusHours(1);
        //table is reserved one hour before reservation starts, and the reservation lasts one hour
        return this.reservationRepository.getReservedTables(timeFrom,timeTo)
                .stream().map(Reservation::getTable)
                .collect(Collectors.toList());
    }

    public List<LocalDateTime> getReservationsForTable(int tableId, LocalDate date){
        return this.reservationRepository.getReservationForTable(tableId, date)
                .stream().map(Reservation::getReservationDateStart)
                .collect(Collectors.toList());
    }
}
