package gymnote.gymnoteapi.exception.workoutExercise;

import gymnote.gymnoteapi.exception.base.BaseException;
import org.springframework.http.HttpStatus;

public class WorkoutExerciseCreationException extends BaseException {
    public WorkoutExerciseCreationException(String message, Throwable cause) {
        super(message, HttpStatus.BAD_REQUEST, cause);
    }
}

