package com.example.italianrestaurant.table;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/tables")
@RequiredArgsConstructor
public class TableController {

    private final TableService tableService;

    @GetMapping
    public ResponseEntity<List<Table>> getNotDeletedTables() {
        return ResponseEntity.ok(tableService.getAllNotDeletedTables());
    }

    @GetMapping("/{nr}")
    public ResponseEntity<Table> getTableByNumber(@PathVariable Long nr) {
        try {
            return ResponseEntity.ok(tableService.getTableByNumber(nr));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "There is no order with id: " + nr, e);
        }
    }

    @PostMapping
    public ResponseEntity<Table> saveTable(@Valid @RequestBody TableDto table) {
        try {
            return ResponseEntity.ok(tableService.saveTable(table));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Table> updateTable(@Valid @RequestBody TableDto table, @PathVariable Long id) {
        try {
            return ResponseEntity.ok(tableService.updateTable(table, id));
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTable(@PathVariable Long id) {
        try {
            tableService.deleteTable(id);
            return ResponseEntity.ok().build();
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "There is no order with id: " + id, e);
        }

    }
}
