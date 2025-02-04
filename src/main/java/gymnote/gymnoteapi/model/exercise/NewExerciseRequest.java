package gymnote.gymnoteapi.model.exercise;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewExerciseRequest {
    private String exerciseName;
    private String type;
    private String description;
    private Integer orderIndex;
}
