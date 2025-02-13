package gymnote.gymnoteapi.model.exerciseSet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateExerciseSetRequest {
    @NotBlank
    private Integer setNumber;

    @NotBlank
    private Integer reps;

    private Double weight;
    private Integer duration;
    private Double distance;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String notes;

    private Integer setOrder;
}


