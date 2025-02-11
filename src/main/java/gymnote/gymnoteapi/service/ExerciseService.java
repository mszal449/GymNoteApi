package gymnote.gymnoteapi.service;

import gymnote.gymnoteapi.entity.Exercise;
import gymnote.gymnoteapi.entity.User;
import gymnote.gymnoteapi.exception.exercise.ExerciseCreationException;
import gymnote.gymnoteapi.exception.exercise.ExerciseDeletionException;
import gymnote.gymnoteapi.exception.exercise.ExerciseNotFoundException;
import gymnote.gymnoteapi.exception.exercise.ExerciseUpdateException;
import gymnote.gymnoteapi.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final UserService userService;

    public Exercise getUserExerciseById(Long exerciseId, Long userId) {
        return exerciseRepository.findByIdAndUserId(exerciseId, userId)
                .orElseThrow(() -> new ExerciseNotFoundException(
                        "Exercise not found with id: " + exerciseId + " for user: " + userId
                ));
    }

    public Exercise createExercise(Exercise exercise, Long userId) {
        User user = userService.findById(userId);

        exercise.setUser(user);
        try {
            return exerciseRepository.save(exercise);
        } catch (DataIntegrityViolationException e) {
            throw new ExerciseCreationException("Failed to create exercise due to data integrity violation", e);
        } catch (Exception e) {
            throw new ExerciseCreationException("Failed to create exercise", e);
        }
    }

    public Exercise updateExercise(Long exerciseId, Long userId, Exercise exerciseData) {
        Exercise exercise = exerciseRepository.findByIdAndUserId(exerciseId, userId)
                .orElseThrow(() -> new ExerciseNotFoundException(
                        "Exercise not found with id: " + exerciseId + " for user: " + userId
                ));

        updateExerciseFields(exercise, exerciseData);

        try {
            return exerciseRepository.save(exercise);
        } catch (DataIntegrityViolationException e) {
            throw new ExerciseUpdateException("Failed to update exercise due to data integrity violation", e);
        } catch (Exception e) {
            throw new ExerciseUpdateException("Failed to update exercise", e);
        }
    }

    public void deleteExercise(Long exerciseId, Long userId) {
        Exercise exercise = exerciseRepository.findByIdAndUserId(exerciseId, userId)
                .orElseThrow(() -> new ExerciseNotFoundException(
                "Exercise not found with id: " + exerciseId + " for user: " + userId
        ));

        try {
            exerciseRepository.delete(exercise);
        } catch (Exception e) {
            throw new ExerciseDeletionException("Failed to delete exercise", e);
        }
    }

    private void updateExerciseFields(Exercise exercise, Exercise exerciseData) {
        Optional.ofNullable(exerciseData.getExerciseName())
                .ifPresent(exercise::setExerciseName);

        Optional.ofNullable(exerciseData.getDescription())
                .ifPresent(exercise::setDescription);

        Optional.ofNullable(exerciseData.getType())
                .ifPresent(exercise::setType);

        Optional.ofNullable(exerciseData.getOrderIndex())
                .ifPresent(exercise::setOrderIndex);
    }

    public List<Exercise> getUserExercises(Long id) {
        return exerciseRepository.findByUserId(id);
    }
}
