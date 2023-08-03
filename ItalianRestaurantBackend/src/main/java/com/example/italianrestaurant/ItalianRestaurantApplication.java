package com.example.italianrestaurant;

import com.example.italianrestaurant.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class ItalianRestaurantApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItalianRestaurantApplication.class, args);
    }

}
