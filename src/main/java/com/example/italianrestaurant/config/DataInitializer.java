package com.example.italianrestaurant.config;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {

//    private final RoleRepository roleRepository;
//    @Value("${add-sample-users-data}")
//    private boolean addSampleUserData;
//
//    @PostConstruct
//    @Transactional
//    public void initUsers() {
//        if(addSampleUserData || roleRepository.count() == 0) {
//            roleRepository.save(new Role("ROLE_ADMIN"));
//        }
//    }
}
