package gymnote.gymnoteapi.exception.workout;

public class WorkoutNotFoundException extends RuntimeException {
    public WorkoutNotFoundException(String message) {
        super(message);
    }
}
