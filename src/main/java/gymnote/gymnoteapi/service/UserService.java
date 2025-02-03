package gymnote.gymnoteapi.service;


import gymnote.gymnoteapi.entity.User;
import gymnote.gymnoteapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Optional<User> updateUser(String existing_username ,User user) {
        Optional<User> foundUser = userRepository.findByUsername(existing_username);

        if (foundUser.isEmpty() || user.getUsername() == null) {
            return Optional.empty();
        }

        User existingUser = foundUser.get();

        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }

        if (user.getUsername() != null) {
            existingUser.setUsername(user.getUsername());
        }

        return Optional.of(userRepository.save(existingUser));
    }

    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}