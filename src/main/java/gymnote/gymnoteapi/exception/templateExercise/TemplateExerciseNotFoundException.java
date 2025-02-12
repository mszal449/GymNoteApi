package gymnote.gymnoteapi.exception.templateExercise;

import gymnote.gymnoteapi.exception.BaseException;
import org.springframework.http.HttpStatus;

public class TemplateExerciseNotFoundException extends BaseException {
    public TemplateExerciseNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
