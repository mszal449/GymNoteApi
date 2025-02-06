package gymnote.gymnoteapi.exception.workout;

public class WorkoutDeletionException extends RuntimeException{
    public WorkoutDeletionException(String message) {
        super(message);
    }

    public WorkoutDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}
