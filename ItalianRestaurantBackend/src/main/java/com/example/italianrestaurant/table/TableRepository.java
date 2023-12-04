package com.example.italianrestaurant.table;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface TableRepository extends JpaRepository<Table, Long> {
    Optional<Table> findByNumberAndDeletedIsFalse(long number);

    Optional<Table> findByNumberAndDeletedIsTrue(long number);
    List<Table> findAllByDeletedIsFalse();

    Optional<Table> findByIdAndDeletedIsFalse(long id);
}
