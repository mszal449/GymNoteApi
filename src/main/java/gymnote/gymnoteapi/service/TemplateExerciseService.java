package gymnote.gymnoteapi.service;

import gymnote.gymnoteapi.entity.Template;
import gymnote.gymnoteapi.entity.TemplateExercise;
import gymnote.gymnoteapi.exception.template.TemplateNotFoundException;
import gymnote.gymnoteapi.exception.templateExercise.*;
import gymnote.gymnoteapi.repository.TemplateExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TemplateExerciseService {
    private final TemplateService templateService;

    private final TemplateExerciseRepository templateExerciseRepository;

    public List<TemplateExercise> getUserTemplateExercises(Long templateId, Long userId) {
        Template template = templateService.getUserTemplateById(templateId, userId);
        return template.getTemplateExercises();
    }

    public TemplateExercise addUserTemplateExercise(Long templateId, Long userId, TemplateExercise templateExerciseData) {
        Template template = templateService.getUserTemplateById(templateId, userId);

        try {
            TemplateExercise exercise = new TemplateExercise();
            exercise.setTemplate(template);
            exercise.setExercise(templateExerciseData.getExercise());

            if(templateExerciseData.getExerciseOrder() == null) {
                exercise.setExerciseOrder(getNextExerciseOrder(template));
            } else {
                exercise.setExerciseOrder(templateExerciseData.getExerciseOrder());
            }

            exercise.validateExerciseOrder();

            return templateExerciseRepository.save(exercise);

        } catch (DataIntegrityViolationException e) {
            throw new TemplateExerciseCreationException("Failed to create template exercise due to data constraints", e);
        } catch (TemplateExerciseDuplicateOrderException e) {
            throw e;
        } catch (Exception e) {
            throw new TemplateExerciseCreationException("Unexpected error creating template exercise", e);
        }
    }

    public TemplateExercise updateUserTemplateExercise(
            Long exerciseId,
            Long templateId,
            Long userId,
            TemplateExercise templateExerciseData) {
        try {
            templateService.getUserTemplateById(templateId, userId);
            TemplateExercise existingExercise = templateExerciseRepository
                    .findByIdAndTemplateId(exerciseId, templateId)
                    .orElseThrow(() -> new TemplateExerciseNotFoundException(
                            "Template exercise not found with id: " + exerciseId));

            if (templateExerciseData.getExerciseOrder() != null) {
                existingExercise.setExerciseOrder(templateExerciseData.getExerciseOrder());
                existingExercise.validateExerciseOrder();
            }

            if (templateExerciseData.getExercise() != null) {
                existingExercise.setExercise(templateExerciseData.getExercise());
            }

            return templateExerciseRepository.save(existingExercise);
        } catch (TemplateExerciseDuplicateOrderException e) {
            throw e;
        } catch (Exception e) {
            throw new TemplateExerciseUpdateException("Failed to update template exercise", e);
        }
    }

    public void deleteUserTemplateExercise(Long exerciseId, Long templateId, Long userId) {
        templateService.getUserTemplateById(templateId, userId);

        TemplateExercise exercise = templateExerciseRepository
                .findByIdAndTemplateId(exerciseId, templateId)
                .orElseThrow(() -> new TemplateExerciseNotFoundException(
                        "Template exercise not found with id: " + exerciseId
                ));

        try {
            templateExerciseRepository.delete(exercise);
        } catch (TemplateNotFoundException e) {
            throw new TemplateExerciseDeletionException("Template not found", e);
        } catch (TemplateExerciseNotFoundException e) {
            throw new TemplateExerciseDeletionException("Template exercise not found.", e);
        } catch (Exception e) {
            throw new TemplateExerciseDeletionException("Failed to delete template exercise", e);
        }
    }

    private Integer getNextExerciseOrder(Template template) {
        return template.getTemplateExercises().stream()
                .mapToInt(TemplateExercise::getExerciseOrder)
                .max()
                .orElse(0) + 1;
    }
}
