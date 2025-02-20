package gymnote.gymnoteapi.controller;

import gymnote.gymnoteapi.config.Const;
import gymnote.gymnoteapi.entity.User;
import gymnote.gymnoteapi.exception.user.UserNotFoundException;
import gymnote.gymnoteapi.model.api.ApiResponse;
import gymnote.gymnoteapi.model.user.CurrentUserUpdateRequest;
import gymnote.gymnoteapi.model.user.UserInfoResponse;
import gymnote.gymnoteapi.security.service.CustomOAuth2User;
import gymnote.gymnoteapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<UserInfoResponse>> getCurrentUserInfo(
            @AuthenticationPrincipal CustomOAuth2User user) {
        User newUser = userService.findById(user.getId());

        UserInfoResponse userInfo = new UserInfoResponse(
            newUser.getId(),
            newUser.getUsername(),
            newUser.getEmail()
        );
        return ResponseEntity.ok(ApiResponse.success(userInfo));
    }

    @GetMapping("/loginSuccess")
    public RedirectView loginUser(
            @AuthenticationPrincipal CustomOAuth2User user) {

        Optional<User> foundUser = userService.findByEmail(user.getEmail());

        if (foundUser.isEmpty()) {
            userService.registerUser(user);
        }

        return new RedirectView(Const.FRONTEND_URL + "/dashboard");
    }

    @PostMapping("/me")
    public ResponseEntity<ApiResponse<UserInfoResponse>> updateCurrentUserInfo(
            @AuthenticationPrincipal CustomOAuth2User user,
            @RequestBody CurrentUserUpdateRequest updateRequest) {
        User newUserData = new User();
        newUserData.setUsername(updateRequest.getUsername());
        newUserData.setEmail(updateRequest.getEmail());

        User updatedUser = userService.updateUser(user.getName(), newUserData)
            .orElseThrow(() -> new UserNotFoundException("User not found"));

        UserInfoResponse userInfo = new UserInfoResponse(
            updatedUser.getId(),
            updatedUser.getUsername(),
            updatedUser.getEmail(),
            "User updated successfully"
        );
        return ResponseEntity.ok(ApiResponse.success(userInfo));
    }
}