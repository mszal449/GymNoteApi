package gymnote.gymnoteapi.service;


import gymnote.gymnoteapi.entity.User;
import gymnote.gymnoteapi.exception.user.UserNotFoundException;
import gymnote.gymnoteapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("User with username " + username + " not found")
        );
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("User with id " + userId + " not found")
        );
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
}