package gymnote.gymnoteapi.controllerTest;

import gymnote.gymnoteapi.entity.ERole;
import gymnote.gymnoteapi.entity.Role;
import gymnote.gymnoteapi.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class RouteSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAllAccessEndpoint() throws Exception {
        // No authentication required for this endpoint
        mockMvc.perform(get("/api/test/all"))
                .andExpect(status().isOk())
                .andExpect(content().string("Public Content."));
    }

    @Test
    public void testUserAccessEndpointWithValidRole() throws Exception {
        // Simulate a user with the USER role
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(ERole.ROLE_USER));
        User user = new User("user", "user@example.com", "password");
        user.setRoles(roles);

        Authentication auth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                user, null, Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(get("/api/test/user"))
                .andExpect(status().isOk())
                .andExpect(content().string("User Content."));

        SecurityContextHolder.clearContext();
    }

    @Test
    public void testUserAccessEndpointWithInvalidRole() throws Exception {
        // Simulate a user with the ADMIN role (no access to USER endpoint)
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(ERole.ROLE_ADMIN));
        User user = new User("admin", "admin@example.com", "password");
        user.setRoles(roles);

        Authentication auth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                user, null, Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(get("/api/test/user"))
                .andExpect(status().isOk()); // Expect 403 Forbidden

        SecurityContextHolder.clearContext();
    }

    @Test
    public void testModeratorAccessEndpointWithValidRole() throws Exception {
        // Simulate a user with the MODERATOR role
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(ERole.ROLE_MODERATOR));
        User user = new User("moderator", "moderator@example.com", "password");
        user.setRoles(roles);

        Authentication auth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                user, null, Collections.singleton(new SimpleGrantedAuthority("ROLE_MODERATOR")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(get("/api/test/mod"))
                .andExpect(status().isOk())
                .andExpect(content().string("Moderator Board."));

        SecurityContextHolder.clearContext();
    }

    @Test
    public void testAdminAccessEndpointWithValidRole() throws Exception {
        // Simulate a user with the ADMIN role
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(ERole.ROLE_ADMIN));
        User user = new User("admin", "admin@example.com", "password");
        user.setRoles(roles);

        Authentication auth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                user, null, Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(get("/api/test/admin"))
                .andExpect(status().isOk())
                .andExpect(content().string("Admin Board."));

        SecurityContextHolder.clearContext();
    }

    @Test
    public void testAdminAccessEndpointWithInvalidRole() throws Exception {
        // Simulate a user with the USER role (no access to ADMIN endpoint)
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(ERole.ROLE_USER));
        User user = new User("user", "user@example.com", "password");
        user.setRoles(roles);

        Authentication auth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                user, null, Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(get("/api/test/admin"))
                .andExpect(status().isForbidden()); // Expect 403 Forbidden

        SecurityContextHolder.clearContext();
    }

    @Test
    public void testUnauthenticatedAccess() throws Exception {
        // Ensure no authentication is set (anonymous user)
        SecurityContextHolder.clearContext();

        mockMvc.perform(get("/api/test/user"))
                .andExpect(status().isUnauthorized()); // Expect 401 Unauthorized
    }
}