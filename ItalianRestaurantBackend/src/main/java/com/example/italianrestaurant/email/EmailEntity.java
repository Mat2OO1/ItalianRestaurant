package com.example.italianrestaurant.email;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class EmailEntity {
    private String from;
    @NotBlank(message = "To is mandatory")
    @Email
    private String to;
    @NotBlank(message = "Subject is mandatory")
    private String subject;
    private String template;
    private String text;
    private Map<String, Object> properties;

    public void addProperty(String key, Object property) {
        if (properties == null) {
            properties = new HashMap<>();
        }
        properties.put(key, property);
    }
}
