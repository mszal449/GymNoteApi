package gymnote.gymnoteapi.model.user;

import lombok.Data;

@Data
public class CurrentUserUpdateRequest {
    private String email;
    private String username;
}