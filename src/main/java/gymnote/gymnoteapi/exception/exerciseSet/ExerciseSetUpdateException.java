package gymnote.gymnoteapi.exception.exerciseSet;

import gymnote.gymnoteapi.exception.base.BaseException;
import org.springframework.http.HttpStatus;

public class ExerciseSetUpdateException extends BaseException {
    public ExerciseSetUpdateException(String message, Throwable cause) {
        super(message, HttpStatus.BAD_REQUEST, cause);
    }
}
