package gymnote.gymnoteapi.exception.template;

import gymnote.gymnoteapi.exception.BaseException;
import org.springframework.http.HttpStatus;

public class TemplateDeletionException extends BaseException {
    public TemplateDeletionException(String message, Throwable cause) {
        super(message, HttpStatus.BAD_REQUEST, cause);
    }
}
