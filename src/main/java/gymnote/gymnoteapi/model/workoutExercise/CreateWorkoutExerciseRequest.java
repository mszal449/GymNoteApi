package gymnote.gymnoteapi.model.workoutExercise;

import gymnote.gymnoteapi.entity.Exercise;
import gymnote.gymnoteapi.entity.WorkoutExercise;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateWorkoutExerciseRequest {
    @NotNull
    private Long exerciseId;

    private Integer exerciseOrder;

    // TODO: Remove when mapper gets added
    public WorkoutExercise toEntity() {
        WorkoutExercise workoutExercise = new WorkoutExercise();
        Exercise exercise = new Exercise();
        exercise.setId(exerciseId);
        workoutExercise.setExercise(exercise);
        workoutExercise.setExerciseOrder(exerciseOrder);
        return workoutExercise;
    }
}

