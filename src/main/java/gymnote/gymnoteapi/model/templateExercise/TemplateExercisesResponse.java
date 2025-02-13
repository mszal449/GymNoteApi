package gymnote.gymnoteapi.model.templateExercise;

import gymnote.gymnoteapi.model.dto.TemplateExerciseDTO;
import lombok.Data;

import java.util.List;

@Data
public class TemplateExercisesResponse {
    private long templateId;
    private int count;
    private List<TemplateExerciseDTO> templateExercises;
}
