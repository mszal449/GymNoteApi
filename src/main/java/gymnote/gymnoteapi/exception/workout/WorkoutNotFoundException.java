package gymnote.gymnoteapi.exception.workout;

import gymnote.gymnoteapi.exception.BaseException;
import org.springframework.http.HttpStatus;

public class WorkoutNotFoundException extends BaseException {
    public WorkoutNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
