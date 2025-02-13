package gymnote.gymnoteapi.service;

import gymnote.gymnoteapi.entity.WorkoutExercise;
import gymnote.gymnoteapi.exception.workout.WorkoutNotFoundException;
import gymnote.gymnoteapi.exception.workoutExercise.WorkoutExerciseNotFoundException;
import gymnote.gymnoteapi.repository.WorkoutExerciseRepository;
import gymnote.gymnoteapi.repository.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkoutExerciseService {
    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final WorkoutRepository workoutRepository;

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

    private void verifyWorkoutBelongsToUser(Long workoutId, Long userId) {
        workoutRepository.findByIdAndUserId(workoutId, userId)
                .orElseThrow(() -> new WorkoutNotFoundException(
                        "Workout not found with id: " + workoutId + " for user: " + userId
                ));
    }
}