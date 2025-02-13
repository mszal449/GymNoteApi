package gymnote.gymnoteapi.model.jwt;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class TokenRefreshRequest {
    @NotBlank
    private String refreshToken;
}
