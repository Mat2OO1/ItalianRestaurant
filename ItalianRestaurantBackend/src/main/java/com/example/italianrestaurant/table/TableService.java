package com.example.italianrestaurant.table;

import com.example.italianrestaurant.meal.Meal;
import com.example.italianrestaurant.order.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TableService {

    private final TableRepository tableRepository;
    private final ModelMapper modelMapper;


    public List<Table> getAllNotDeletedTables() {
        return tableRepository.findAllByDeletedIsFalse();
    }

    public Table getTableById(Long id) {
        return tableRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Table saveTable(TableDto table) {
        Optional<Table> dbTable = tableRepository.findByNumberAndDeletedIsFalse(table.getNumber());
        if (dbTable.isPresent())
            throw new IllegalArgumentException("There already exists a table with number " + table.getNumber());

        Table mappedTable = modelMapper.map(table, Table.class);
        return tableRepository.save(mappedTable);
    }

    public Table updateTable(TableDto table, Long id) {
        Optional<Table> dbTable = tableRepository.findByNumberAndDeletedIsFalse(table.getNumber());
        if (dbTable.isPresent() && !dbTable.get().getId().equals(id))
            throw new IllegalArgumentException("There already exists a table with number " + table.getNumber());
        Table mappedTable = modelMapper.map(table, Table.class);
        mappedTable.setId(id);
        return tableRepository.save(mappedTable);
    }

    public void deleteTable(Long id) {
        Table table = tableRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        table.setDeleted(true);
        tableRepository.save(table);
    }

    public Table getTableByNumber(long tableNr) {
        return tableRepository.findByNumberAndDeletedIsFalse(tableNr).orElseThrow(EntityNotFoundException::new);
    }
}
