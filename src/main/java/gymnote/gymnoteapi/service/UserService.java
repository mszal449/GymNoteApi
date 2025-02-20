package gymnote.gymnoteapi.service;


import gymnote.gymnoteapi.entity.ERole;
import gymnote.gymnoteapi.entity.Role;
import gymnote.gymnoteapi.entity.User;
import gymnote.gymnoteapi.exception.user.UserNotFoundException;
import gymnote.gymnoteapi.model.auth.SignupRequest;
import gymnote.gymnoteapi.model.auth.UserAlreadyExistsException;
import gymnote.gymnoteapi.model.jwt.MessageResponse;
import gymnote.gymnoteapi.repository.RoleRepository;
import gymnote.gymnoteapi.repository.UserRepository;
import gymnote.gymnoteapi.security.service.CustomOAuth2User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public User registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new UserAlreadyExistsException("Error: Email is already in use.");
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail());

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        return userRepository.save(user);
    }

    public User registerUser(CustomOAuth2User oAuth2User) {
        if (userRepository.existsByEmail(oAuth2User.getEmail())) {
            throw new UserAlreadyExistsException("Error: Email is already in use.");
        }

        // Create new user's account
        User user = new User(oAuth2User.getName(),
                oAuth2User.getEmail());

        Set<Role> roles = new HashSet<>();

        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);

        user.setRoles(roles);
        return userRepository.save(user);
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

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}