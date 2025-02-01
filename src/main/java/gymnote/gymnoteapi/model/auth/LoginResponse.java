package gymnote.gymnoteapi.model.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private final String accessToken;
    private final String refreshToken;
}
