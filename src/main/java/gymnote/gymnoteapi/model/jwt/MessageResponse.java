package gymnote.gymnoteapi.model.jwt;


import lombok.Data;



@Data
public class MessageResponse {
    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }
}