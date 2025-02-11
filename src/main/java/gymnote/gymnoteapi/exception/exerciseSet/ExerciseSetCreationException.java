package gymnote.gymnoteapi.exception.exerciseSet;

import org.springframework.dao.DataIntegrityViolationException;

public class ExerciseSetCreationException extends RuntimeException{
    public ExerciseSetCreationException(String message, DataIntegrityViolationException e) {
        super(message);
    }

    public ExerciseSetCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
