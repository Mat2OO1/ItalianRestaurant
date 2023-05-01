package com.example.italianrestaurant.delivery;

import com.example.italianrestaurant.exceptions.InvalidEntityException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final ModelMapper modelMapper;

    public Delivery addDelivery(DeliveryDto deliveryDto) throws InvalidEntityException {
        Delivery delivery = modelMapper.map(deliveryDto, Delivery.class);
        if (!isDeliveryValid(delivery)) throw new InvalidEntityException();
        return deliveryRepository.save(delivery);
    }

    public boolean isDeliveryValid(Delivery delivery) {
        return delivery.getAddress() != null &&
                delivery.getCity() != null &&
                delivery.getPostalCode() != null &&
                delivery.getFloor() != null;
    }
}
