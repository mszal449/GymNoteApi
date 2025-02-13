package gymnote.gymnoteapi.model.templateExercise;

import lombok.Data;

@Data
public class UpdateTemplateExerciseRequest {
    private Long templateId;
    private Long exerciseId;
    private Integer exerciseOrder;
}
