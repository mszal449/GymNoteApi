package gymnote.gymnoteapi.model.auth;


import gymnote.gymnoteapi.model.annotations.Password;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import gymnote.gymnoteapi.config.ValidationConstants;

@Data
@Builder
public class RegistrationRequest {
    @NotBlank(message = "Email is required.")
    @Pattern(regexp = ValidationConstants.EMAIL_PATTERN , message = "Invalid email format")
    private String email;

    @NotBlank(message = "Username is required")
    @Pattern(regexp = ValidationConstants.USERNAME_PATTERN, message = "Username must be 3-20 characters")
    private String username;

    @Password
    private String password;
}


