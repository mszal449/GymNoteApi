package gymnote.gymnoteapi.model.dto;

import gymnote.gymnoteapi.entity.Exercise;
import gymnote.gymnoteapi.model.exercise.NewExerciseRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExerciseDTO {
    private Long id;
    private Long userId;
    private String exerciseName;
    private String type; // Use String instead of EExerciseType for flexibility
    private String description;
    private Integer orderIndex;

    public ExerciseDTO() {}

    public ExerciseDTO(Exercise exercise) {
        this.id = exercise.getId();
        this.userId = exercise.getUser().getId();
        this.exerciseName = exercise.getExerciseName();
        this.type = exercise.getType() != null ? exercise.getType().name() : null;
        this.description = exercise.getDescription();
        this.orderIndex = exercise.getOrderIndex();
    }

    public ExerciseDTO(NewExerciseRequest newExerciseRequest) {
        this.exerciseName = newExerciseRequest.getExerciseName();
        this.type = newExerciseRequest.getType() != null ? newExerciseRequest.getType() : null;
        this.description = newExerciseRequest.getDescription();
        this.orderIndex = newExerciseRequest.getOrderIndex();
    }
}
