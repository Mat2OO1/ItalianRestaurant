package com.example.italianrestaurant.passwordreset.passwordtoken;

import com.example.italianrestaurant.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tokens")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class PasswordToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;
    private Long expiryTime;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    @ToString.Exclude
    private User user;
}
