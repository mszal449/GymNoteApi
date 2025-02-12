package gymnote.gymnoteapi.exception.templateExercise;

import gymnote.gymnoteapi.exception.BaseException;
import org.springframework.http.HttpStatus;

public class TemplateExerciseCreationException extends BaseException {
    public TemplateExerciseCreationException(String message, Throwable cause) {
        super(message, HttpStatus.BAD_REQUEST, cause);
    }
}
