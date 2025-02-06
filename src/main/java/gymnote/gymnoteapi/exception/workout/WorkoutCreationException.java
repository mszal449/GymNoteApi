package gymnote.gymnoteapi.exception.workout;

public class WorkoutCreationException extends RuntimeException {
    public WorkoutCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
