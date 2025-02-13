package gymnote.gymnoteapi.model.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank
    private String username; // Use username

    @NotBlank
    private String password;
}