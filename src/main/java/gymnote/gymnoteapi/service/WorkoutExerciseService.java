package gymnote.gymnoteapi.service;

import gymnote.gymnoteapi.entity.Exercise;
import gymnote.gymnoteapi.entity.Workout;
import gymnote.gymnoteapi.entity.WorkoutExercise;
import gymnote.gymnoteapi.exception.workout.WorkoutNotFoundException;
import gymnote.gymnoteapi.exception.workoutExercise.WorkoutExerciseCreationException;
import gymnote.gymnoteapi.exception.workoutExercise.WorkoutExerciseDeletionException;
import gymnote.gymnoteapi.exception.workoutExercise.WorkoutExerciseNotFoundException;
import gymnote.gymnoteapi.exception.workoutExercise.WorkoutExerciseUpdateException;
import gymnote.gymnoteapi.repository.WorkoutExerciseRepository;
import gymnote.gymnoteapi.repository.WorkoutRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkoutExerciseService {
    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final WorkoutRepository workoutRepository;
    private final ExerciseService exerciseService;

    public List<WorkoutExercise> getWorkoutExercises(Long workoutId, Long userId) {
        verifyWorkoutBelongsToUser(workoutId, userId);
        return workoutExerciseRepository.findByWorkoutIdOrderByRealOrder(workoutId);
    }

    public WorkoutExercise getWorkoutExerciseById(Long exerciseId, Long workoutId, Long userId) {
        verifyWorkoutBelongsToUser(workoutId, userId);
        return workoutExerciseRepository.findByIdAndWorkoutId(exerciseId, workoutId)
                .orElseThrow(() -> new WorkoutExerciseNotFoundException(
                        "WorkoutExercise not found with id: " + exerciseId + " for workout: " + workoutId
                ));
    }

    @Transactional
    public WorkoutExercise createWorkoutExercise(WorkoutExercise workoutExercise, Long workoutId, Long userId) {
        Workout workout = verifyWorkoutBelongsToUser(workoutId, userId);

        // Set the workout reference
        workoutExercise.setWorkout(workout);

        // Verify that the exercise exists
        Exercise exercise = exerciseService.getUserExerciseById(workoutExercise.getExercise().getId(), userId);
        workoutExercise.setExercise(exercise);


        try {
            return workoutExerciseRepository.save(workoutExercise);
        } catch (DataIntegrityViolationException e) {
            throw new WorkoutExerciseCreationException("Failed to create workout exercise due to data integrity violation", e);
        } catch (Exception e) {
            throw new WorkoutExerciseCreationException("Failed to create workout exercise", e);
        }
    }

    @Transactional
    public WorkoutExercise updateWorkoutExercise(Long exerciseId, Long workoutId, Long userId, WorkoutExercise workoutExerciseData) {
        verifyWorkoutBelongsToUser(workoutId, userId);

        WorkoutExercise existingExercise = workoutExerciseRepository.findByIdAndWorkoutId(exerciseId, workoutId)
                .orElseThrow(() -> new WorkoutExerciseNotFoundException(
                        "WorkoutExercise not found with id: " + exerciseId + " for workout: " + workoutId
                ));

        updateWorkoutExerciseFields(existingExercise, workoutExerciseData, userId);

        try {
            return workoutExerciseRepository.save(existingExercise);
        } catch (DataIntegrityViolationException e) {
            throw new WorkoutExerciseUpdateException("Failed to update workout exercise due to data integrity violation", e);
        } catch (Exception e) {
            throw new WorkoutExerciseUpdateException("Failed to update workout exercise", e);
        }
    }

    @Transactional
    public void deleteWorkoutExercise(Long exerciseId, Long workoutId, Long userId) {
        verifyWorkoutBelongsToUser(workoutId, userId);

        WorkoutExercise exercise = workoutExerciseRepository.findByIdAndWorkoutId(exerciseId, workoutId)
                .orElseThrow(() -> new WorkoutExerciseNotFoundException(
                        "WorkoutExercise not found with id: " + exerciseId + " for workout: " + workoutId
                ));

        try {
            workoutExerciseRepository.delete(exercise);
            reorderRemainingExercises(workoutId);
        } catch (Exception e) {
            throw new WorkoutExerciseDeletionException("Failed to delete workout exercise", e);
        }
    }

    private Workout verifyWorkoutBelongsToUser(Long workoutId, Long userId) {
        return workoutRepository.findByIdAndUserId(workoutId, userId)
                .orElseThrow(() -> new WorkoutNotFoundException(
                        "Workout not found with id: " + workoutId + " for user: " + userId
                ));
    }

    private void updateWorkoutExerciseFields(WorkoutExercise existingExercise, WorkoutExercise workoutExerciseData, Long userId) {
        Optional.ofNullable(workoutExerciseData.getExercise())
                .ifPresent(exercise -> {
                    Exercise existingExerciseEntity = exerciseService.getUserExerciseById(exercise.getId(), userId);
                    existingExercise.setExercise(existingExerciseEntity);
                });

        Optional.ofNullable(workoutExerciseData.getRealOrder())
                .ifPresent(existingExercise::setRealOrder);

    }

    @Transactional
    protected void reorderRemainingExercises(Long workoutId) {
        List<WorkoutExercise> exercises = workoutExerciseRepository.findByWorkoutIdOrderByRealOrder(workoutId);
        for (int i = 0; i < exercises.size(); i++) {
            exercises.get(i).setRealOrder(i + 1);
        }
        workoutExerciseRepository.saveAll(exercises);
    }
}