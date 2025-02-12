package gymnote.gymnoteapi.exception.template;

import gymnote.gymnoteapi.exception.BaseException;
import org.springframework.http.HttpStatus;

public class TemplateNotFoundException extends BaseException {
    public TemplateNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
