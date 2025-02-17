package gymnote.gymnoteapi.security;

import gymnote.gymnoteapi.model.auth.GoogleOAuth2UserInfo;
import gymnote.gymnoteapi.model.auth.OAuth2UserInfo;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String provider, Map<String, Object> attributes) {
        if (provider.equalsIgnoreCase("google")) {
            return new GoogleOAuth2UserInfo(attributes);
            // Add cases for other providers like "github", "facebook" etc.
        }
        throw new IllegalArgumentException("Unsupported provider: " + provider);
    }
}