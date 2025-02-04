package gymnote.gymnoteapi.exception.exercise;

public class ExerciseNotFoundException extends RuntimeException{
    public ExerciseNotFoundException(String message) {
        super(message);
    }
}
