package gymnote.gymnoteapi.model.dto;

import lombok.Data;

@Data
public class WorkoutExerciseDTO {
    private Long id;
    private Long workoutId;
    private Long exerciseId;
    private String exerciseName;
    private String exerciseDescription;
    private Integer realOrder;
//    private List<ExerciseSetDTO> sets;
}
