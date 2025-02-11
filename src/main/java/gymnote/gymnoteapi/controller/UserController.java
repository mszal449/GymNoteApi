package gymnote.gymnoteapi.controller;

import gymnote.gymnoteapi.entity.User;
import gymnote.gymnoteapi.exception.UserNotFoundException;
import gymnote.gymnoteapi.model.api.ApiResponse;
import gymnote.gymnoteapi.model.user.CurrentUserUpdateRequest;
import gymnote.gymnoteapi.model.user.UserInfoResponse;
import gymnote.gymnoteapi.security.service.UserDetailsImpl;
import gymnote.gymnoteapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getCurrentUserInfo(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            User user = userService.findById(userDetails.getId());

            UserInfoResponse userInfo = new UserInfoResponse(
                user.getUsername(),
                user.getEmail()
            );
            return ResponseEntity.ok(ApiResponse.success(userInfo));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("User not found"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to fetch user information"));
        }
    }

    @PostMapping("/me")
    public ResponseEntity<ApiResponse<UserInfoResponse>> updateCurrentUserInfo(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CurrentUserUpdateRequest updateRequest) {
        try {
            User newUserData = new User();
            newUserData.setUsername(updateRequest.getUsername());
            newUserData.setEmail(updateRequest.getEmail());

            User updatedUser = userService.updateUser(userDetails.getUsername(), newUserData)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

            UserInfoResponse userInfo = new UserInfoResponse(
                updatedUser.getUsername(),
                updatedUser.getEmail(),
                "User updated successfully"
            );
            return ResponseEntity.ok(ApiResponse.success(userInfo));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("User not found"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to update user information"));
        }
    }
}