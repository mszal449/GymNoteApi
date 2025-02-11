package gymnote.gymnoteapi.model.dto;

import lombok.Data;

@Data
public class WorkoutExerciseDTO {
    private Long id;
    private Long workoutId;
    private Long exerciseId;
    private Integer exerciseOrder;
//    private List<ExerciseSetDTO> sets;
}
