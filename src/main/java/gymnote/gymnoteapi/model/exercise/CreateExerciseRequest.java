package gymnote.gymnoteapi.model.exercise;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateExerciseRequest {
    @NotBlank(message = "Exercise name is required")
    private String exerciseName;

    @NotBlank(message = "Exercise type is required")
    private String type;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
}
