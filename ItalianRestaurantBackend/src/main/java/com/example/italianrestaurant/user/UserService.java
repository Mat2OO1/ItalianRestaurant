package com.example.italianrestaurant.user;

import com.example.italianrestaurant.auth.UserDto;
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
        return user.orElseThrow(EntityNotFoundException::new);
    }

    public void updatePassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        if (!userRepository.existsByEmail(user.getEmail()))
            throw new EntityNotFoundException();
        userRepository.save(user);

    }

    public User updateUser(UserDto userDto) {
        User user = getUserByEmail(userDto.getEmail());
        modelMapper.map(userDto, user);
        userRepository.save(user);
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        userRepository.deleteById(id);
    }
}
