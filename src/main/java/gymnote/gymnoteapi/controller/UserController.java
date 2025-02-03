package gymnote.gymnoteapi.controller;

import gymnote.gymnoteapi.entity.User;
import gymnote.gymnoteapi.model.user.CurrentUserUpdateRequest;
import gymnote.gymnoteapi.model.user.UserInfoResponse;
import gymnote.gymnoteapi.security.jwt.JwtUtils;
import gymnote.gymnoteapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
@RequestMapping("/api/user")
public class UserController {
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getCurrentUserInfo() {
        // Get the authenticated user's details
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username;
        if (principal instanceof UserDetails) {
            // If the principal is a UserDetails object, get the username
            username = ((UserDetails) principal).getUsername();
        } else {
            // If the principal is a String (e.g., JWT subject), use it directly
            username = principal.toString();
        }
        Optional<User> user = userService.findByUsername(username);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UserInfoResponse("User not found"));
        }

        String email = user.get().getEmail();

        return ResponseEntity.status(HttpStatus.OK).body(new UserInfoResponse(username, email));
    }

    @PostMapping("/me")
    public ResponseEntity<UserInfoResponse> updateCurrentUserInfo(@RequestBody CurrentUserUpdateRequest currentUserUpdateRequest) {
        // TODO: UPDATE PRINCIPAL FOR AUTHORIZATION
        // Get the authenticated user's details
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username;
        if (principal instanceof UserDetails) {
            // If the principal is a UserDetails object, get the username
            username = ((UserDetails) principal).getUsername();
        } else {
            // If the principal is a String (e.g., JWT subject), use it directly
            username = principal.toString();
        }
        try {
            User newUserData = new User();
            newUserData.setUsername(currentUserUpdateRequest.getUsername());
            newUserData.setEmail(currentUserUpdateRequest.getEmail());

            Optional<User> user = userService.updateUser(username, newUserData);

            if (user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UserInfoResponse("User not found"));
            }
            User userModel = user.get();

            return ResponseEntity.status(HttpStatus.OK).body(new UserInfoResponse(
                    userModel.getUsername(),
                    userModel.getEmail(),
                    "User updated successfully."
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UserInfoResponse("Invalid email"));
        }

    }
}
