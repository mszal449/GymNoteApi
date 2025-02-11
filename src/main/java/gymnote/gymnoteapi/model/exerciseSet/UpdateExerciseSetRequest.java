package gymnote.gymnoteapi.model.exerciseSet;

import lombok.Data;

@Data
public class UpdateExerciseSetRequest {
    private Integer setNumber;
    private Integer reps;
    private Double weight;
    private Integer duration;
    private Double distance;
    private String notes;
    private Integer setOrder;
}



