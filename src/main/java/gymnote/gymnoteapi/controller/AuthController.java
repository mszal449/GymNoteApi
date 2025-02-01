package gymnote.gymnoteapi.controller;

import gymnote.gymnoteapi.exception.UserAlreadyExistsException;
import gymnote.gymnoteapi.exception.ValidationException;
import gymnote.gymnoteapi.model.auth.RegistrationRequest;
import gymnote.gymnoteapi.model.response.ErrorResponse;
import gymnote.gymnoteapi.model.auth.LoginRequest;
import gymnote.gymnoteapi.model.auth.LoginResponse;
import gymnote.gymnoteapi.security.UserPrincipal;
import gymnote.gymnoteapi.service.RegistrationService;
import gymnote.gymnoteapi.service.RefreshTokenService;
import gymnote.gymnoteapi.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final RegistrationService registrationService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Validated LoginRequest request) {
        // Authenticate the user
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        var principal = (UserPrincipal) auth.getPrincipal();

        // Generate access token using JwtService
        var accessToken = jwtService.generateAccessToken(principal);

        // Generate refresh token using RefreshTokenService
        var refreshToken = refreshTokenService.createRefreshToken(principal.getUserId());

        // Build the response
        var loginResponse = LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/auth/register")
    public ResponseEntity<String> registerUser(@RequestBody RegistrationRequest request) {
        registrationService.registerUser(request);
        return ResponseEntity.ok("User registered successfully");
    }
}