package gymnote.gymnoteapi.exception.exercise;

import gymnote.gymnoteapi.exception.base.BaseException;
import org.springframework.http.HttpStatus;

public class ExerciseNotFoundException extends BaseException {
    public ExerciseNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
