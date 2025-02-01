package gymnote.gymnoteapi.exception;

public class ValidationException extends RuntimeException{
    public ValidationException(String message) {
        super(message);
    }
}
