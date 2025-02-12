package gymnote.gymnoteapi.service;

import gymnote.gymnoteapi.entity.Exercise;
import gymnote.gymnoteapi.entity.User;
import gymnote.gymnoteapi.exception.exercise.ExerciseCreationException;
import gymnote.gymnoteapi.exception.exercise.ExerciseDeletionException;
import gymnote.gymnoteapi.exception.exercise.ExerciseNotFoundException;
import gymnote.gymnoteapi.exception.exercise.ExerciseUpdateException;
import gymnote.gymnoteapi.repository.ExerciseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final UserService userService;

    public Exercise getUserExerciseById(Long exerciseId, Long userId) {
        log.debug("Fetching exercise with id: {} for user: {}", exerciseId, userId);
        return exerciseRepository.findByIdAndUserId(exerciseId, userId)
                .orElseThrow(() -> new ExerciseNotFoundException(
                    String.format("Exercise not found with id: %d for user: %d", exerciseId, userId)
                ));
    }

    @Transactional
    public Exercise createExercise(Exercise exercise, Long userId) {
        log.debug("Creating exercise for user: {}", userId);
        User user = userService.findById(userId);
        exercise.setUser(user);

        try {
            return exerciseRepository.save(exercise);
        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation while creating exercise", e);
            throw new ExerciseCreationException("Failed to create exercise: duplicate entry", e);
        } catch (Exception e) {
            log.error("Unexpected error while creating exercise", e);
            throw new ExerciseCreationException("Failed to create exercise: internal error", e);
        }
    }

    @Transactional
    public Exercise updateExercise(Long exerciseId, Long userId, Exercise exerciseData) {
        log.debug("Updating exercise: {} for user: {}", exerciseId, userId);
        Exercise exercise = getUserExerciseById(exerciseId, userId);
        
        updateExerciseFields(exercise, exerciseData);

        try {
            return exerciseRepository.save(exercise);
        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation while updating exercise", e);
            throw new ExerciseUpdateException("Failed to update exercise: duplicate entry", e);
        } catch (Exception e) {
            log.error("Unexpected error while updating exercise", e);
            throw new ExerciseUpdateException("Failed to update exercise: internal error", e);
        }
    }

    @Transactional
    public void deleteExercise(Long exerciseId, Long userId) {
        log.debug("Deleting exercise: {} for user: {}", exerciseId, userId);
        Exercise exercise = getUserExerciseById(exerciseId, userId);

        try {
            exerciseRepository.delete(exercise);
        } catch (Exception e) {
            log.error("Failed to delete exercise", e);
            throw new ExerciseDeletionException("Failed to delete exercise: internal error", e);
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

    public List<Exercise> getUserExercises(Long userId) {
        log.debug("Fetching all exercises for user: {}", userId);
        return exerciseRepository.findByUserId(userId);
    }
}