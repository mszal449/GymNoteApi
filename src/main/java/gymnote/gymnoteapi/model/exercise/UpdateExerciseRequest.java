package gymnote.gymnoteapi.model.exercise;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateExerciseRequest {
    private String exerciseName;

    private String type;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    private Integer orderIndex;
}


