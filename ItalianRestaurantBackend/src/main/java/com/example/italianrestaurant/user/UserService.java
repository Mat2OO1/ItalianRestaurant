package com.example.italianrestaurant.user;

import com.example.italianrestaurant.auth.AuthenticationResponse;
import com.example.italianrestaurant.security.JwtService;
import com.example.italianrestaurant.security.UserPrincipal;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public User getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));
    }

    public void updatePassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        if (!userRepository.existsByEmail(user.getEmail()))
            throw new EntityNotFoundException();
        userRepository.save(user);

    }

    public AuthenticationResponse updateUser(UserDto userDto, String userEmail) {
        User user = getUserByEmail(userEmail);
        if (user.getProvider() == AuthProvider.local) {
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setEmail(userDto.getEmail());
        }
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setNewsletter(userDto.isNewsletter());
        User saved = userRepository.save(user);
        UserPrincipal userPrincipal = UserPrincipal.create(saved);
        var jwtToken = jwtService.generateToken(userPrincipal);
        var expiration = jwtService.getTokenExpiration(jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .expiration(expiration)
                .role(saved.getRole())
                .build();
    }

    public void changePassword(PasswordChangeRequest request, String email) throws WrongPasswordException {
        User user = getUserByEmail(email);
        if (passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            updatePassword(user, request.getNewPassword());
        } else {
            throw new WrongPasswordException("Current password is invalid");
        }
    }

    public void deleteAccount(String password, String username) {
        User user = getUserByEmail(username);
        if (passwordEncoder.matches(password, user.getPassword())) {
            userRepository.delete(user);
        } else {
            throw new EntityNotFoundException();
        }
    }
}
