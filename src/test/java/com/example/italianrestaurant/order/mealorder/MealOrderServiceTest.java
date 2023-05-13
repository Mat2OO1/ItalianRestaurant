package com.example.italianrestaurant.order.mealorder;

import com.example.italianrestaurant.Utils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MealOrderServiceTest {
    @Mock
    private MealOrderRepository mealOrderRepository;
    @InjectMocks
    private MealOrderService mealOrderService;

    @Test
    void shouldAddMealOrder() {
        //given
        MealOrder mealOrder = Utils.getMealOrder();
        given(mealOrderRepository.save(any())).willReturn(mealOrder);
        //when
        mealOrderService.addMealOrder(mealOrder);
        //then
        verify(mealOrderRepository, times(1)).save(mealOrder);
    }
}
