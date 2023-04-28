package com.example.italianrestaurant.email;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class Email {
    private String from;
    private String to;
    private String subject;
    private String template;
    private Map<String, Object> properties;

    public void addProperty(String key, Object property) {
        if (properties == null) {
            properties = new HashMap<>();
        }
        properties.put(key, property);
    }
}
