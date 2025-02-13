package gymnote.gymnoteapi.mapper;

import gymnote.gymnoteapi.entity.Exercise;
import gymnote.gymnoteapi.entity.Template;
import gymnote.gymnoteapi.entity.TemplateExercise;
import gymnote.gymnoteapi.model.templateExercise.CreateTemplateExerciseRequest;
import gymnote.gymnoteapi.model.templateExercise.UpdateTemplateExerciseRequest;

public class TemplateExerciseMapper {
    public static TemplateExercise toEntity(CreateTemplateExerciseRequest request) {
        TemplateExercise templateExercise = new TemplateExercise();

        Template template = new Template();
        template.setId(request.getTemplateId());
        templateExercise.setTemplate(template);

        Exercise exercise = new Exercise();
        exercise.setId(request.getExerciseId());

        templateExercise.setTemplate(template);
        templateExercise.setExercise(exercise);
        templateExercise.setExerciseOrder(request.getExerciseOrder());

        return templateExercise;
    }

    public static TemplateExercise toEntity(UpdateTemplateExerciseRequest request) {
        TemplateExercise templateExercise = new TemplateExercise();

        Template template = new Template();
        template.setId(request.getTemplateId());
        templateExercise.setTemplate(template);

        Exercise exercise = new Exercise();
        exercise.setId(request.getExerciseId());

        templateExercise.setTemplate(template);
        templateExercise.setExercise(exercise);
        templateExercise.setExerciseOrder(request.getExerciseOrder());

        return templateExercise;
    }
}
