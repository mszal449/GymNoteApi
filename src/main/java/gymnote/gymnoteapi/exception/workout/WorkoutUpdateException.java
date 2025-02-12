package gymnote.gymnoteapi.exception.workout;

import gymnote.gymnoteapi.exception.BaseException;
import org.springframework.http.HttpStatus;

public class WorkoutUpdateException extends BaseException {
    public WorkoutUpdateException(String message, Throwable cause) {
        super(message, HttpStatus.BAD_REQUEST, cause);
    }
}
