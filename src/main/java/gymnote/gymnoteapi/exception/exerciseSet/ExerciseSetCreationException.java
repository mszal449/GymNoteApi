package gymnote.gymnoteapi.exception.exerciseSet;

import gymnote.gymnoteapi.exception.base.BaseException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

public class ExerciseSetCreationException extends BaseException {
    public ExerciseSetCreationException(String message, DataIntegrityViolationException e) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
