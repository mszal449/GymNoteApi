package gymnote.gymnoteapi.model.templateExercise;

import gymnote.gymnoteapi.entity.Exercise;
import gymnote.gymnoteapi.entity.Template;
import gymnote.gymnoteapi.entity.TemplateExercise;
import lombok.Data;

@Data
public class UpdateTemplateExerciseRequest {
    private Long templateId;
    private Long exerciseId;
    private Integer exerciseOrder;

    // TODO: remove
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
