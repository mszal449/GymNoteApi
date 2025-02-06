package gymnote.gymnoteapi.exception.workout;

public class WorkoutUpdateException extends RuntimeException {
    public WorkoutUpdateException(String message) {
        super(message);
    }

    public WorkoutUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
