package com.example.italianrestaurant.table;

import com.example.italianrestaurant.table.reservation.ReservationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TableService {

    private final TableRepository tableRepository;
    private final ModelMapper modelMapper;


    public List<Table> getAllTables() {
        return tableRepository.findAll();
    }

    public Table getTableById(Long id) {
        return tableRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Table saveTable(TableDto table) {
        Table mappedTable = modelMapper.map(table, Table.class);
        return tableRepository.save(mappedTable);
    }

    public void deleteTable(Long id) {
        tableRepository.deleteById(id);
    }
}
