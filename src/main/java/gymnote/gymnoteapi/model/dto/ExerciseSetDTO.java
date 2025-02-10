package gymnote.gymnoteapi.model.dto;

import lombok.Data;

@Data
public class ExerciseSetDTO {
    private Long id;
    private Long workoutExerciseId;
    private Integer setNumber;
    private Integer reps;
    private Double weight;
    private Integer duration;
    private Double distance;
    private String notes;
    private Integer setOrder;
}