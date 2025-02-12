package gymnote.gymnoteapi.exception.workout;

import gymnote.gymnoteapi.exception.BaseException;
import org.springframework.http.HttpStatus;

public class WorkoutCreationException extends BaseException {
    public WorkoutCreationException(String message, Throwable cause) {
        super(message, HttpStatus.BAD_REQUEST,cause);
    }
}
