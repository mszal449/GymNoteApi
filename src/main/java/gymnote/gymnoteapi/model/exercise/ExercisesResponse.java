package gymnote.gymnoteapi.model.exercise;

import gymnote.gymnoteapi.model.dto.ExerciseDTO;
import lombok.Data;

import java.util.List;

@Data
public class ExercisesResponse {
    private List<ExerciseDTO> exercises;
    private int count;
}
