package gymnote.gymnoteapi.exception.exercise;

import gymnote.gymnoteapi.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ExerciseDeletionException extends BaseException {
    public ExerciseDeletionException(String message, Throwable cause) {
        super(message, HttpStatus.BAD_REQUEST, cause);
    }
}
