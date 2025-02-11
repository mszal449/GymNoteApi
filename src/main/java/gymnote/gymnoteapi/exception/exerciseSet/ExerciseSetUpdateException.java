package gymnote.gymnoteapi.exception.exerciseSet;

public class ExerciseSetUpdateException extends RuntimeException{
    public ExerciseSetUpdateException(String message) {
        super(message);
    }

    public ExerciseSetUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
