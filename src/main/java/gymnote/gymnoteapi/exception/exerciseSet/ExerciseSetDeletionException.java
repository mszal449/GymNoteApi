package gymnote.gymnoteapi.exception.exerciseSet;

public class ExerciseSetDeletionException extends RuntimeException{
    public ExerciseSetDeletionException(String message) {
        super(message);
    }

    public ExerciseSetDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}
