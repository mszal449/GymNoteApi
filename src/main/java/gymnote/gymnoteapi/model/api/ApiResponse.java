package gymnote.gymnoteapi.model.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private T data;
    private String message;
    private LocalDateTime timestamp;
    private Integer count;

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setData(data);
        response.setMessage("Success");
        response.setTimestamp(LocalDateTime.now());
        if (data instanceof List<?>) {
            response.setCount(((List<?>) data).size());
        }
        return response;
    }

    public static <T> ApiResponse<T> error(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setMessage(message);
        response.setTimestamp(LocalDateTime.now());
        return response;
    }
}