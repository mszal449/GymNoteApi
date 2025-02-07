package gymnote.gymnoteapi.model.workoutExercise;

import gymnote.gymnoteapi.entity.Exercise;
import gymnote.gymnoteapi.entity.WorkoutExercise;
import lombok.Data;

@Data
public class UpdateWorkoutExerciseRequest {
    private Long exerciseId;
    private Integer realOrder;

    public WorkoutExercise toEntity() {
        WorkoutExercise workoutExercise = new WorkoutExercise();
        if (exerciseId != null) {
            Exercise exercise = new Exercise();
            exercise.setId(exerciseId);
            workoutExercise.setExercise(exercise);
        }
        workoutExercise.setRealOrder(realOrder);
        

        return workoutExercise;
    }
}
