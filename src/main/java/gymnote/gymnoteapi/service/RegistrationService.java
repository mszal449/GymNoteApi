package gymnote.gymnoteapi.service;

import gymnote.gymnoteapi.entity.User;
import gymnote.gymnoteapi.exception.UserAlreadyExistsException;
import gymnote.gymnoteapi.exception.ValidationException;
import gymnote.gymnoteapi.model.auth.RegistrationRequest;
import gymnote.gymnoteapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(RegistrationRequest request) {
        // Validate the request
        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            throw new ValidationException("Username is required");
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new ValidationException("Password is required");
        }
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new ValidationException("Email is required");
        }

        // Check if the username or email already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setAccountNonLocked(true);
        user.setRoles(List.of("ROLE_USER"));

        userRepository.save(user);
    }
}
