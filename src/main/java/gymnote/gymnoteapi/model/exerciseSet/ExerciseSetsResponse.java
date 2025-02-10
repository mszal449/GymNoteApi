package gymnote.gymnoteapi.model.exerciseSet;

import gymnote.gymnoteapi.model.dto.ExerciseSetDTO;
import lombok.Data;

import java.util.List;

@Data
public class ExerciseSetsResponse {
    private List<ExerciseSetDTO> sets;
    private int count;
}
