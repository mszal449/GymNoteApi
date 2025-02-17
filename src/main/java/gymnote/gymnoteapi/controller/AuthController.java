package gymnote.gymnoteapi.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import gymnote.gymnoteapi.entity.*;
import gymnote.gymnoteapi.exception.token.TokenRefreshException;
import gymnote.gymnoteapi.model.auth.LoginRequest;
import gymnote.gymnoteapi.model.auth.OAuth2UserInfo;
import gymnote.gymnoteapi.model.jwt.JwtResponse;
import gymnote.gymnoteapi.model.jwt.MessageResponse;
import gymnote.gymnoteapi.model.jwt.TokenRefreshRequest;
import gymnote.gymnoteapi.model.jwt.TokenRefreshResponse;
import gymnote.gymnoteapi.repository.RoleRepository;
import gymnote.gymnoteapi.repository.UserRepository;
import gymnote.gymnoteapi.security.OAuth2UserInfoFactory;
import gymnote.gymnoteapi.security.jwt.JwtUtils;
import gymnote.gymnoteapi.model.auth.SignupRequest;
import gymnote.gymnoteapi.security.service.RefreshTokenService;
import gymnote.gymnoteapi.security.service.UserDetailsImpl;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder encoder;
  private final JwtUtils jwtUtils;
  private final RefreshTokenService refreshTokenService;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    String jwt = jwtUtils.generateJwtToken(userDetails);

    List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

    RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

    return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
            userDetails.getUsername(), userDetails.getEmail(), roles));
  }

  @PostMapping("/refreshtoken")
  public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
    String requestRefreshToken = request.getRefreshToken();

    return refreshTokenService.findByToken(requestRefreshToken)
            .map(refreshTokenService::verifyExpiration)
            .map(RefreshToken::getUser)
            .map(user -> {
              String token = jwtUtils.generateTokenFromUsername(user.getUsername());
              return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
            })
            .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                    "Refresh token is not in database!"));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(),
            signUpRequest.getEmail(),
            encoder.encode(signUpRequest.getPassword()));

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
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }


  @GetMapping("/oauth2/callback/{provider}")
  public ResponseEntity<?> authenticateOAuth2User(
        @PathVariable String provider,
        Authentication authentication)
    {

    if (!(authentication instanceof OAuth2AuthenticationToken)) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid authentication type"));
    }

      OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
      Map<String, Object> attributes = oauth2User.getAttributes();

      OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(provider, attributes);

      User user = userRepository.findByEmail(userInfo.getEmail())
              .orElseGet(() -> createNewOAuth2User(userInfo));

      String jwt = jwtUtils.generateTokenFromUsername(user.getUsername());
      RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

      List<String> roles = user.getRoles().stream()
              .map(role -> role.getName().name())
              .collect(Collectors.toList());

      return ResponseEntity.ok(new JwtResponse(jwt,
              refreshToken.getToken(),
              user.getId(),
              user.getUsername(), 
              user.getEmail(),
              roles));
    }

  private User createNewOAuth2User(OAuth2UserInfo userInfo) {
    User user = new User();
    user.setUsername(generateUniqueUsername(userInfo.getName()));
    user.setEmail(userInfo.getEmail());
    user.setProvider(AuthProvider.valueOf(userInfo.getProvider().toUpperCase()));
    user.setProviderId(userInfo.getId());

    // Set default role as USER
    Set<Role> roles = new HashSet<>();
    Role userRole = roleRepository.findByName(ERole.ROLE_USER)
            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    roles.add(userRole);
    user.setRoles(roles);

    return userRepository.save(user);
  }

  private String generateUniqueUsername(String name) {
    String baseUsername = name.toLowerCase().replaceAll("\\s+", "");
    String username = baseUsername;
    int counter = 1;

    while (userRepository.existsByUsername(username)) {
      username = baseUsername + counter++;
    }

    return username;
  }
}