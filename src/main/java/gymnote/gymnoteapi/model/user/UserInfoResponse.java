package gymnote.gymnoteapi.model.user;

import lombok.Data;

@Data
public class UserInfoResponse {
    private Long id;
    private String email;
    private String username;
    private String message;

    public UserInfoResponse(Long id, String username, String email, String message) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.message = message;

    }public UserInfoResponse(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public UserInfoResponse(String message) {
        this.message = message;
    }
}