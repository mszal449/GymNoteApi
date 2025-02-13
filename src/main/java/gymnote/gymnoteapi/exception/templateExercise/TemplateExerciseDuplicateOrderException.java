package gymnote.gymnoteapi.exception.templateExercise;

import gymnote.gymnoteapi.exception.base.BaseException;
import org.springframework.http.HttpStatus;

public class TemplateExerciseDuplicateOrderException extends BaseException {
    public TemplateExerciseDuplicateOrderException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
