package gymnote.gymnoteapi.model.template;

import gymnote.gymnoteapi.model.dto.TemplateExerciseDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TemplateExercisesResponse {
    private long templateId;
    private int count;
    private List<TemplateExerciseDTO> templateExercises;
}
