package gymnote.gymnoteapi.service;

import gymnote.gymnoteapi.entity.User;
import gymnote.gymnoteapi.security.UserPrincipal;
import gymnote.gymnoteapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return UserPrincipal.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .build();
    }
}