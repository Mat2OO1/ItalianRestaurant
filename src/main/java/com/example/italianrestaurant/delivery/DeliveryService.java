package com.example.italianrestaurant.delivery;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final ModelMapper modelMapper;

    public Delivery addDelivery(DeliveryDto deliveryDto) {
        Delivery delivery = modelMapper.map(deliveryDto, Delivery.class);
        delivery.setDeliveryDate(LocalDateTime.now());
        return deliveryRepository.save(delivery);
    }
}
