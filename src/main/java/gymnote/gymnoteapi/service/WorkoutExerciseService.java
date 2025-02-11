package gymnote.gymnoteapi.service;

import gymnote.gymnoteapi.entity.Exercise;
import gymnote.gymnoteapi.entity.Workout;
import gymnote.gymnoteapi.entity.WorkoutExercise;
import gymnote.gymnoteapi.exception.workout.WorkoutNotFoundException;
import gymnote.gymnoteapi.exception.workoutExercise.WorkoutExerciseCreationException;
import gymnote.gymnoteapi.exception.workoutExercise.WorkoutExerciseDuplicateOrderException;
import gymnote.gymnoteapi.exception.workoutExercise.WorkoutExerciseNotFoundException;
import gymnote.gymnoteapi.repository.WorkoutExerciseRepository;
import gymnote.gymnoteapi.repository.WorkoutRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkoutExerciseService {
    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final WorkoutRepository workoutRepository;
    private final ExerciseService exerciseService;

    public List<WorkoutExercise> getWorkoutExercises(Long workoutId, Long userId) {
        verifyWorkoutBelongsToUser(workoutId, userId);
        return workoutExerciseRepository.findByWorkoutIdOrderByExerciseOrder(workoutId);
    }

    public WorkoutExercise getWorkoutExerciseById(Long exerciseId, Long workoutId, Long userId) {
        verifyWorkoutBelongsToUser(workoutId, userId);
        return workoutExerciseRepository.findByIdAndWorkoutId(exerciseId, workoutId)
                .orElseThrow(() -> new WorkoutExerciseNotFoundException(
                        "WorkoutExercise not found with id: " + exerciseId + " for workout: " + workoutId
                ));
    }

    @Transactional
    public WorkoutExercise createWorkoutExercise(WorkoutExercise workoutExercise, Long workoutId, Long exerciseId, Long userId) {
        Workout workout = verifyWorkoutBelongsToUser(workoutId, userId);
        workoutExercise.setWorkout(workout);

        Exercise exercise = exerciseService.getUserExerciseById(exerciseId, userId);
        workoutExercise.setExercise(exercise);
        workoutExercise.setExerciseOrder(workoutExercise.getTemplateExercise().getExerciseOrder());

        try {
            return workoutExerciseRepository.save(workoutExercise);
        } catch (DataIntegrityViolationException e) {
            throw new WorkoutExerciseCreationException("Failed to create workout exercise due to data integrity violation", e);
        } catch (WorkoutExerciseDuplicateOrderException e) {
            throw e;
        } catch (Exception e) {
            throw new WorkoutExerciseCreationException("Failed to create workout exercise", e);
        }
    }

    private Workout verifyWorkoutBelongsToUser(Long workoutId, Long userId) {
        return workoutRepository.findByIdAndUserId(workoutId, userId)
                .orElseThrow(() -> new WorkoutNotFoundException(
                        "Workout not found with id: " + workoutId + " for user: " + userId
                ));
    }
}