package gymnote.gymnoteapi.exception.template;

import gymnote.gymnoteapi.exception.BaseException;
import org.springframework.http.HttpStatus;

public class TemplateUpdateException extends BaseException {
    public TemplateUpdateException(String message, Throwable cause) {
        super(message, HttpStatus.BAD_REQUEST, cause);
    }
}
