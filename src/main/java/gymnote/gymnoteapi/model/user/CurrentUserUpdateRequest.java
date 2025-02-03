package gymnote.gymnoteapi.model.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrentUserUpdateRequest {
    private String email;
    private String username;
}