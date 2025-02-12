package gymnote.gymnoteapi.exception.exercise;

import gymnote.gymnoteapi.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ExerciseCreationException extends BaseException {
    public ExerciseCreationException(String message, Throwable cause) {
        super(message, HttpStatus.BAD_REQUEST, cause);
    }
}