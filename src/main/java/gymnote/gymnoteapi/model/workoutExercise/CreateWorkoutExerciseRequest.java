package gymnote.gymnoteapi.model.workoutExercise;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateWorkoutExerciseRequest {
    @NotNull
    private Long exerciseId;

    private Integer exerciseOrder;
}

