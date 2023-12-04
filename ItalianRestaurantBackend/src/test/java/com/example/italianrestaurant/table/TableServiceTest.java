package com.example.italianrestaurant.table;

import com.example.italianrestaurant.Utils;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    @Mock
    private TableRepository tableRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TableService tableService;

    @Test
    void shouldGetAllTables() {
        //given
        val tables = List.of(Utils.getTable());
        given(tableRepository.findAllByDeletedIsFalse()).willReturn(tables);
        //when
        val allTables = tableService.getAllNotDeletedTables();
        //then
        assertThat(allTables).isEqualTo(tables);
    }

    @Test
    void shouldGetEmptyListOfTables() {
        //given
        val tables = List.of();
        //when
        val allTables = tableService.getAllNotDeletedTables();
        //then
        assertThat(allTables).isEqualTo(tables);
    }

    @Test
    void shouldGetTableById() {
        //given
        val table = Utils.getTable();
        table.setId(1L);
        given(tableRepository.findById(table.getId())).willReturn(java.util.Optional.of(table));
        //when
        val tableById = tableService.getTableById(table.getId());
        //then
        assertThat(tableById).isEqualTo(table);
    }

    @Test
    void shouldNotGetTableById() {
        //given
        val id = 1L;
        given(tableRepository.findById(id)).willReturn(java.util.Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> tableService.getTableById(id))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldSaveTable() {
        //given
        val table = Utils.getTable();
        val tableDto = Utils.getTableDto();
        given(tableRepository.save(any())).willReturn(table);
        given(modelMapper.map(tableDto, Table.class)).willReturn(table);
        //when
        val savedTable = tableService.saveTable(tableDto);
        //then
        assertThat(savedTable).isEqualTo(table);
    }

    @Test
    void shouldDeleteTable() {
        //given
        val id = 1L;
        val table = Utils.getTable();
        when(tableRepository.findById(any(Long.class))).thenReturn(Optional.of(table));
        //when
        tableService.deleteTable(id);
        //then
        verify(tableRepository, times(1)).save(any(Table.class));
    }

}
