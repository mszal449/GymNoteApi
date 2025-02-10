package gymnote.gymnoteapi.service;

import gymnote.gymnoteapi.entity.ExerciseSet;
import gymnote.gymnoteapi.entity.WorkoutExercise;
import gymnote.gymnoteapi.exception.exerciseSet.ExerciseSetCreationException;
import gymnote.gymnoteapi.exception.exerciseSet.ExerciseSetDeletionException;
import gymnote.gymnoteapi.exception.exerciseSet.ExerciseSetNotFoundException;
import gymnote.gymnoteapi.exception.exerciseSet.ExerciseSetUpdateException;
import gymnote.gymnoteapi.repository.ExerciseSetRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseSetService {
    private final ExerciseSetRepository exerciseSetRepository;
    private final WorkoutExerciseService workoutExerciseService;

    public List<ExerciseSet> getExerciseSets(Long workoutId, Long exerciseId, Long userId) {
        return exerciseSetRepository.findAllValidated(exerciseId, workoutId, userId);
    }

    public ExerciseSet getExerciseSetById(Long setId, Long exerciseId, Long workoutId, Long userId) {
        return exerciseSetRepository.findOneValidated(setId, exerciseId, workoutId, userId)
                .orElseThrow(() -> new ExerciseSetNotFoundException("Set not found: " + setId));
    }


    @Transactional
    public ExerciseSet createExerciseSet(ExerciseSet exerciseSet, Long exerciseId, Long workoutId, Long userId) {
        WorkoutExercise workoutExercise = workoutExerciseService.getWorkoutExerciseById(exerciseId, workoutId, userId);
        exerciseSet.setWorkoutExercise(workoutExercise);

        try {
            return exerciseSetRepository.save(exerciseSet);
        } catch (DataIntegrityViolationException e) {
            throw new ExerciseSetCreationException("Failed to create exercise set", e);
        }
    }

    @Transactional
    public ExerciseSet updateExerciseSet(Long setId, ExerciseSet exerciseSetData, Long exerciseId, Long workoutId, Long userId) {
        ExerciseSet existingSet = getExerciseSetById(setId, exerciseId, workoutId, userId);
        updateExerciseSetFields(existingSet, exerciseSetData);

        try {
            return exerciseSetRepository.save(existingSet);
        } catch (DataIntegrityViolationException e) {
            throw new ExerciseSetUpdateException("Failed to update exercise set", e);
        }
    }

    @Transactional
    public void deleteExerciseSet(Long setId, Long exerciseId, Long workoutId, Long userId) {
        ExerciseSet set = getExerciseSetById(setId, exerciseId, workoutId, userId);
        Integer deletedOrder = set.getSetOrder();

        try {
            int deleted = exerciseSetRepository.deleteValidated(setId, exerciseId, workoutId, userId);
            if (deleted > 0) {
                exerciseSetRepository.reorderAfterDelete(exerciseId, workoutId, userId, deletedOrder);
            }
        } catch (Exception e) {
            throw new ExerciseSetDeletionException("Failed to delete exercise set", e);
        }
    }


    private void updateExerciseSetFields(ExerciseSet existingSet, ExerciseSet setData) {
        if (setData.getReps() != null) existingSet.setReps(setData.getReps());
        if (setData.getWeight() != null) existingSet.setWeight(setData.getWeight());
        if (setData.getDuration() != null) existingSet.setDuration(setData.getDuration());
        if (setData.getDistance() != null) existingSet.setDistance(setData.getDistance());
        if (setData.getNotes() != null) existingSet.setNotes(setData.getNotes());
        if (setData.getSetOrder() != null) existingSet.setSetOrder(setData.getSetOrder());
    }

    @Transactional
    protected void reorderRemainingSets(Long exerciseId, Long workoutId, Long userId) {
        List<ExerciseSet> sets = exerciseSetRepository.findSetsForReordering(exerciseId, workoutId, userId);
        for (int i = 0; i < sets.size(); i++) {
            exerciseSetRepository.updateSetOrder(sets.get(i).getId(), i + 1);
        }
    }
}