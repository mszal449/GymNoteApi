package gymnote.gymnoteapi.exception.workoutExercise;

import gymnote.gymnoteapi.exception.base.BaseException;
import org.springframework.http.HttpStatus;

public class WorkoutExerciseNotFoundException extends BaseException {
    public WorkoutExerciseNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
