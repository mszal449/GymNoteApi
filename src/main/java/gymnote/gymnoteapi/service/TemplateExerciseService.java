package gymnote.gymnoteapi.service;

import gymnote.gymnoteapi.entity.Template;
import gymnote.gymnoteapi.entity.TemplateExercise;
import gymnote.gymnoteapi.exception.template.TemplateCreationException;
import gymnote.gymnoteapi.exception.template.TemplateNotFoundException;
import gymnote.gymnoteapi.exception.templateExercise.*;
import gymnote.gymnoteapi.repository.TemplateExerciseRepository;
import gymnote.gymnoteapi.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TemplateExerciseService {
    private final TemplateService templateService;
    private final TemplateRepository templateRepository;

    private final TemplateExerciseRepository templateExerciseRepository;

    public List<TemplateExercise> getUserTemplateExercises(Long templateId, Long userId) {
        Template template = templateService.getUserTemplateById(templateId, userId);
        return template.getTemplateExercises();
    }

    public List<TemplateExercise> getTemplateExercises(Long templateId) {
        Template template = templateService.getTemplateById(templateId);
        return template.getTemplateExercises();
    }

    public TemplateExercise addUserTemplateExercise(Long templateId, Long userId, TemplateExercise templateExerciseData) {
        // Retrieve the template, which will throw TemplateNotFoundException if not found
        Template template = templateService.getUserTemplateById(templateId, userId);

        try {
            // Create new TemplateExercise
            TemplateExercise exercise = new TemplateExercise();
            exercise.setTemplate(template);
            exercise.setExercise(templateExerciseData.getExercise());
            exercise.setExerciseOrder(templateExerciseData.getExerciseOrder());

            // Save the new TemplateExercise
            return templateExerciseRepository.save(exercise);

        } catch (DataIntegrityViolationException e) {
            // More specific exception for database-level constraints
            throw new TemplateExerciseCreationException("Failed to create template exercise due to data constraints", e);
        } catch (Exception e) {
            // Catch-all for any unexpected errors
            throw new TemplateExerciseCreationException("Unexpected error creating template exercise", e);
        }
    }

    public TemplateExercise updateUserTemplateExercise(
            Long exerciseId,
            Long templateId,
            Long userId,
            TemplateExercise templateExerciseData) {
        try {
            // Check if template belongs to user
            Template template = templateService.getUserTemplateById(templateId, userId);

            TemplateExercise existingExercise = templateExerciseRepository
                    .findByIdAndTemplateId(exerciseId, templateId)
                    .orElseThrow(() -> new TemplateExerciseNotFoundException(
                            "Template exercise not found with id: " + exerciseId
                    ));

            // Check if exercise order is being changed
            Integer newExerciseOrder = templateExerciseData.getExerciseOrder();
            if (newExerciseOrder != null && !newExerciseOrder.equals(existingExercise.getExerciseOrder())) {
                // Check if this exercise order already exists for another exercise in the template
                boolean orderExists = template.getTemplateExercises().stream()
                        .anyMatch(te -> te.getExerciseOrder() != null &&
                                te.getExerciseOrder().equals(newExerciseOrder) &&
                                !te.getId().equals(exerciseId));

                if (orderExists) {
                    throw new TemplateExerciseDuplicateOrderException(
                            "Exercise order " + newExerciseOrder + " already exists in this template"
                    );
                }
            }

            Optional.ofNullable(templateExerciseData.getExercise())
                    .ifPresent(existingExercise::setExercise);

            Optional.ofNullable(newExerciseOrder)
                    .ifPresent(existingExercise::setExerciseOrder);

            // Save updated exercise
            return templateExerciseRepository.save(existingExercise);

        } catch (TemplateNotFoundException e) {
            throw new TemplateExerciseUpdateException("Template not found", e);
        } catch (DataIntegrityViolationException e) {
            throw new TemplateExerciseUpdateException(
                "Failed to update template exercise due to data constraints",
                e
            );
        } catch (Exception e) {
            throw new TemplateExerciseUpdateException(
                "Unexpected error updating template exercise",
                e
            );
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
}
