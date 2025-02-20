package gymnote.gymnoteapi.security;

import gymnote.gymnoteapi.entity.User;
import gymnote.gymnoteapi.security.service.CustomOAuth2User;
import gymnote.gymnoteapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserService userService; // Your user service to handle DB operations

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        try {
            return processOAuth2User(userRequest, oauth2User);
        } catch (Exception ex) {
            throw new OAuth2AuthenticationException(ex.getMessage());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oauth2User) {
        String clientName = userRequest.getClientRegistration().getClientName();
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(oauth2User);

        // Check if user exists
        String email = customOAuth2User.getEmail();
        if (email == null || email.isEmpty()) {
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }

        // Find or create user in your system
        User user = userService.findByEmail(email)
                .orElseGet(() -> userService.registerUser(customOAuth2User));

        customOAuth2User.setId(user.getId());
        return customOAuth2User;
    }
}