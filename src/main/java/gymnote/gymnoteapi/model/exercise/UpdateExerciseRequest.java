package gymnote.gymnoteapi.model.exercise;

import gymnote.gymnoteapi.entity.EExerciseType;
import gymnote.gymnoteapi.entity.Exercise;
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

    public Exercise toEntity() {
        Exercise exercise = new Exercise();
        exercise.setExerciseName(this.exerciseName);
        exercise.setDescription(this.description);
        exercise.setType(EExerciseType.valueOf(this.type));
        exercise.setOrderIndex(this.orderIndex);
        return exercise;
    }
}


