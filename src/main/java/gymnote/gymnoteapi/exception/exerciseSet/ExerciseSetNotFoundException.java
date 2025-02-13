package gymnote.gymnoteapi.exception.exerciseSet;

import gymnote.gymnoteapi.exception.base.BaseException;
import org.springframework.http.HttpStatus;

public class ExerciseSetNotFoundException extends BaseException {
    public ExerciseSetNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
