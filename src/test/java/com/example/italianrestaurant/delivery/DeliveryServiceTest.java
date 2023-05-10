package com.example.italianrestaurant.delivery;

import com.example.italianrestaurant.Utils;
import com.example.italianrestaurant.exceptions.InvalidEntityException;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class DeliveryServiceTest {

    @Mock
    private DeliveryRepository deliveryRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private DeliveryService deliveryService;


    @Test
    void shouldAddDelivery() throws InvalidEntityException {

        // given
        val deliveryDto = Utils.getDeliveryDto();
        val mappedDelivery = Utils.getDelivery();
        val dbDelivery = Utils.getDelivery();
        dbDelivery.setId(1L);

        given(modelMapper.map(deliveryDto, Delivery.class)).willReturn(mappedDelivery);
        given(deliveryRepository.save(mappedDelivery)).willReturn(dbDelivery);

        // when
        val returnedDelivery = deliveryService.addDelivery(deliveryDto);

        // then
        verify(deliveryRepository).save(mappedDelivery);
        assertThat(returnedDelivery).isEqualTo(dbDelivery);
    }
}
