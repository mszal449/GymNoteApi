package gymnote.gymnoteapi.model.templateExercise;

import gymnote.gymnoteapi.entity.Exercise;
import gymnote.gymnoteapi.entity.Template;
import gymnote.gymnoteapi.entity.TemplateExercise;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTemplateExerciseRequest {
    @NotNull
    private Long templateId;

    @NotNull
    private Long exerciseId;

    @Min(value = 0, message = "Exercise order must be greater than or equal to 0")
    private Integer exerciseOrder;

    // TODO: Remove when mapper is added
    public TemplateExercise toEntity() {
        TemplateExercise templateExercise = new TemplateExercise();

        Template template = new Template();
        template.setId(templateId);
        templateExercise.setTemplate(template);

        Exercise exercise = new Exercise();
        exercise.setId(exerciseId);

        templateExercise.setTemplate(template);
        templateExercise.setExercise(exercise);
        templateExercise.setExerciseOrder(exerciseOrder);

        return templateExercise;
    }
}
