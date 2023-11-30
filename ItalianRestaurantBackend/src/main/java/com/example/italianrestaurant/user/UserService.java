package com.example.italianrestaurant.user;

import com.example.italianrestaurant.auth.RegisterRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

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

    public User updateUser(UserDto userDto, String userEmail) {
        User user = getUserByEmail(userEmail);
        if (user.getProvider() == AuthProvider.local) {
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setEmail(userDto.getEmail());
        }
        user.setPhoneNumber(userDto.getPhoneNumber());
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        userRepository.deleteById(id);
    }

    public void changePassword(PasswordChangeRequest request, String email) throws WrongPasswordException {
        User user = getUserByEmail(email);
        if (passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            updatePassword(user, request.getNewPassword());
        } else {
            throw new WrongPasswordException("Password is wrong");
        }
    }
}
