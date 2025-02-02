package gymnote.gymnoteapi;

import gymnote.gymnoteapi.controller.AuthController;
import gymnote.gymnoteapi.entity.ERole;
import gymnote.gymnoteapi.entity.RefreshToken;
import gymnote.gymnoteapi.entity.Role;
import gymnote.gymnoteapi.entity.User;
import gymnote.gymnoteapi.exception.TokenRefreshException;
import gymnote.gymnoteapi.model.auth.LoginRequest;
import gymnote.gymnoteapi.model.auth.SignupRequest;
import gymnote.gymnoteapi.model.jwt.JwtResponse;
import gymnote.gymnoteapi.model.jwt.MessageResponse;
import gymnote.gymnoteapi.model.jwt.TokenRefreshRequest;
import gymnote.gymnoteapi.model.jwt.TokenRefreshResponse;
import gymnote.gymnoteapi.repository.RoleRepository;
import gymnote.gymnoteapi.repository.UserRepository;
import gymnote.gymnoteapi.security.jwt.JwtUtils;
import gymnote.gymnoteapi.security.service.RefreshTokenService;
import gymnote.gymnoteapi.security.service.UserDetailsImpl;
import org.aspectj.bridge.Message;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder encoder;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthController authController;

    private UserDetailsImpl userDetails;
    private User user;
    private Role userRole;
    private RefreshToken refreshToken;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@test.com");
        user.setPassword("encoded_password");

        userRole = new Role(ERole.ROLE_USER);
        userRole.setId(1);

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        // Initialize UserDetails
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(ERole.ROLE_USER.name())
        );

        userDetails = new UserDetailsImpl(
                1L,
                "testUser",
                "test@test.com",
                "password",
                authorities
        );

        // Initialize RefreshToken
        refreshToken = new RefreshToken();
        refreshToken.setToken("refresh_token");
        refreshToken.setUser(user);
    }

    @Test
    void authenticateUser_Success() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");


        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(userDetails)).thenReturn("jwt_token");
        when(refreshTokenService.createRefreshToken(1L)).thenReturn(refreshToken);

        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        assertInstanceOf(JwtResponse.class, response.getBody());
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertEquals("jwt_token", jwtResponse.getToken());
        assertEquals("refresh_token", jwtResponse.getRefreshToken());
        assertEquals("testUser", jwtResponse.getUsername());
        assertEquals("test@test.com", jwtResponse.getEmail());
    }

    @Test
    void registerUser_WithExistingUsername() {
        SignupRequest request = new SignupRequest();
        request.setUsername("existingusername");
        request.setEmail("existingtest@test.com");
        request.setPassword("password");

        Set<String> roles = new HashSet<>();
        roles.add("user");
        request.setRole(roles);

        when(userRepository.existsByUsername("existingusername")).thenReturn(true);

        ResponseEntity<?> signupResponse = authController.registerUser(request);

        assertInstanceOf(MessageResponse.class, signupResponse.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, signupResponse.getStatusCode());

        MessageResponse messageResponse = (MessageResponse) signupResponse.getBody();
        assertEquals("Error: Username is already taken!", messageResponse.getMessage());
    }

    @Test
    void registerUser_Success() {
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setUsername("newUser");
        signUpRequest.setEmail("new@test.com");
        signUpRequest.setPassword("password");
        Set<String> roles = new HashSet<>();
        roles.add("user");
        signUpRequest.setRole(roles);

        Role userRole = new Role(ERole.ROLE_USER);
        userRole.setId(1);

        when(userRepository.existsByUsername("newUser")).thenReturn(false);
        when(userRepository.existsByEmail("new@test.com")).thenReturn(false);
        when(encoder.encode("password")).thenReturn("encoded_password");
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(userRole));

        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        MessageResponse messageResponse = (MessageResponse) response.getBody();
        assert messageResponse != null;

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(messageResponse.getMessage().contains("User registered successfully"));
        verify(userRepository).save(any(User.class));
    }


    @Test
    void registerUser_WithAdminRole() {
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setUsername("adminUser");
        signUpRequest.setEmail("admin@test.com");
        signUpRequest.setPassword("password");
        Set<String> roles = new HashSet<>();
        roles.add("admin");
        signUpRequest.setRole(roles);

        Role adminRole = new Role(ERole.ROLE_ADMIN);
        adminRole.setId(2);

        when(userRepository.existsByUsername("adminUser")).thenReturn(false);
        when(userRepository.existsByEmail("admin@test.com")).thenReturn(false);
        when(encoder.encode("password")).thenReturn("encoded_password");
        when(roleRepository.findByName(ERole.ROLE_ADMIN)).thenReturn(Optional.of(adminRole));

        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        MessageResponse messageResponse = (MessageResponse) response.getBody();
        assert messageResponse != null;

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(messageResponse.getMessage().contains("User registered successfully"));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_WithModeratorRole() {
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setUsername("modUser");
        signUpRequest.setEmail("mod@test.com");
        signUpRequest.setPassword("password");
        Set<String> roles = new HashSet<>();
        roles.add("mod");
        signUpRequest.setRole(roles);

        Role modRole = new Role(ERole.ROLE_MODERATOR);
        modRole.setId(3);

        when(userRepository.existsByUsername("modUser")).thenReturn(false);
        when(userRepository.existsByEmail("mod@test.com")).thenReturn(false);
        when(encoder.encode("password")).thenReturn("encoded_password");
        when(roleRepository.findByName(ERole.ROLE_MODERATOR)).thenReturn(Optional.of(modRole));

        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        MessageResponse messageResponse = (MessageResponse) response.getBody();

        assert messageResponse != null;
        assertTrue(messageResponse.getMessage().contains("User registered successfully"));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void refreshToken_Success() {
        TokenRefreshRequest refreshRequest = new TokenRefreshRequest();
        refreshRequest.setRefreshToken("valid_refresh_token");

        when(refreshTokenService.findByToken("valid_refresh_token"))
                .thenReturn(Optional.of(refreshToken));
        when(refreshTokenService.verifyExpiration(refreshToken))
                .thenReturn(refreshToken);
        when(jwtUtils.generateTokenFromUsername(anyString()))
                .thenReturn("new_jwt_token");

        ResponseEntity<?> response = authController.refreshToken(refreshRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        TokenRefreshResponse tokenResponse = (TokenRefreshResponse) response.getBody();
        assertNotNull(tokenResponse);
        assertEquals("new_jwt_token", tokenResponse.getAccessToken());
        assertEquals("valid_refresh_token", tokenResponse.getRefreshToken());
    }

    @Test
    void refreshToken_InvalidToken() {
        TokenRefreshRequest refreshRequest = new TokenRefreshRequest();
        refreshRequest.setRefreshToken("invalid_refresh_token");

        when(refreshTokenService.findByToken("invalid_refresh_token"))
                .thenReturn(Optional.empty());

        assertThrows(TokenRefreshException.class,
                () -> authController.refreshToken(refreshRequest));
    }
}