package gymnote.gymnoteapi.model.user;

import lombok.Data;

@Data
public class UserInfoResponse {
    private String email;
    private String username;
    private String message;

    public UserInfoResponse(String username, String email, String message) {
        this.username = username;
        this.email = email;
        this.message = message;

    }public UserInfoResponse(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public UserInfoResponse(String message) {
        this.message = message;
    }
}