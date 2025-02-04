package gymnote.gymnoteapi.model.dto;

import gymnote.gymnoteapi.entity.TemplateExercise;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemplateExerciseDTO {
    private Long id;
    private Long templateId;
    private Long exerciseId;
    private Integer exerciseOrder;

    public TemplateExerciseDTO() {}

    public TemplateExerciseDTO(TemplateExercise templateExercise) {
        this.id = templateExercise.getId();
        this.templateId = templateExercise.getTemplate().getId();
        this.exerciseId = templateExercise.getExercise().getId();
        this.exerciseOrder = templateExercise.getExerciseOrder();
    }
}
