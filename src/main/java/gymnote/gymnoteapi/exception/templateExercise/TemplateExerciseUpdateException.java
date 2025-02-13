package gymnote.gymnoteapi.exception.templateExercise;

import gymnote.gymnoteapi.exception.base.BaseException;
import org.springframework.http.HttpStatus;

public class TemplateExerciseUpdateException extends BaseException {
    public TemplateExerciseUpdateException(String message, Throwable cause) {
        super(message, HttpStatus.BAD_REQUEST, cause);
    }
}
