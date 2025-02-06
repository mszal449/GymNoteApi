package gymnote.gymnoteapi.model.workoutExercise;

import gymnote.gymnoteapi.model.dto.WorkoutExerciseDTO;
import lombok.Data;

import java.util.List;

@Data
public class WorkoutExercisesResponse {
    private List<WorkoutExerciseDTO> exercises;
    private int count;
}
