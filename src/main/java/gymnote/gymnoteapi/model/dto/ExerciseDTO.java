package gymnote.gymnoteapi.model.dto;

import gymnote.gymnoteapi.entity.EExerciseType;
import gymnote.gymnoteapi.entity.Exercise;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExerciseDTO {
    private Long id;
    private Long userId;
    private String exerciseName;
    private EExerciseType type;
    private String description;
    private Integer orderIndex;

    public ExerciseDTO(Exercise exercise) {
        this.id = exercise.getId();
        this.userId = exercise.getUser().getId();
        this.exerciseName = exercise.getExerciseName();
        this.type = exercise.getType();
        this.description = exercise.getDescription();
        this.orderIndex = exercise.getOrderIndex();
    }
}
