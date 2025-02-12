package gymnote.gymnoteapi.service;


import gymnote.gymnoteapi.entity.User;
import gymnote.gymnoteapi.exception.user.UserNotFoundException;
import gymnote.gymnoteapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

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