package gymnote.gymnoteapi.service;

import gymnote.gymnoteapi.entity.User;
import gymnote.gymnoteapi.entity.Workout;
import gymnote.gymnoteapi.exception.UserNotFoundException;
import gymnote.gymnoteapi.exception.workout.WorkoutCreationException;
import gymnote.gymnoteapi.exception.workout.WorkoutDeletionException;
import gymnote.gymnoteapi.exception.workout.WorkoutNotFoundException;
import gymnote.gymnoteapi.exception.workout.WorkoutUpdateException;
import gymnote.gymnoteapi.repository.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final UserService userService;

    public List<Workout> getUserWorkouts(Long userId) {
        User user = userService.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        return workoutRepository.findByUserId(userId);
    }

    public Workout getUserWorkoutById(Long workoutId, Long userId) {
        return workoutRepository.findByIdAndUserId(workoutId, userId)
            .orElseThrow(() -> new WorkoutNotFoundException(
                "Workout not found with id: " + workoutId + " for user: " + userId
            ));
    }

    public Workout createWorkout(Workout workout, Long userId) {
        User user = userService.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        workout.setUser(user);
        try {
            return workoutRepository.save(workout);
        } catch (DataIntegrityViolationException e) {
            throw new WorkoutCreationException("Failed to create workout due to data integrity violation", e);
        } catch (Exception e) {
            throw new WorkoutCreationException("Failed to create workout", e);
        }
    }

    public Workout updateWorkout(Long workoutId, Long userId, Workout workoutData) {
        Workout workout = workoutRepository.findByIdAndUserId(workoutId, userId)
            .orElseThrow(() -> new WorkoutNotFoundException(
                "Workout not found with id: " + workoutId + " for user: " + userId
            ));

        updateWorkoutFields(workout, workoutData);

        try {
            return workoutRepository.save(workout);
        } catch (DataIntegrityViolationException e) {
            throw new WorkoutUpdateException("Failed to update workout due to data integrity violation", e);
        } catch (Exception e) {
            throw new WorkoutUpdateException("Failed to update workout", e);
        }
    }

    @Transactional
    public void deleteWorkout(Long workoutId, Long userId) {
        Workout workout = workoutRepository.findByIdAndUserId(workoutId, userId)
            .orElseThrow(() -> new WorkoutNotFoundException(
                "Workout not found with id: " + workoutId + " for user: " + userId
            ));

        try {
            workoutRepository.delete(workout);
        } catch (Exception e) {
            throw new WorkoutDeletionException("Failed to delete workout", e);
        }
    }

    private void updateWorkoutFields(Workout existingWorkout, Workout workoutData) {
        Optional.ofNullable(workoutData.getName())
            .ifPresent(existingWorkout::setName);
        Optional.ofNullable(workoutData.getStartTime())
            .ifPresent(existingWorkout::setStartTime);
        Optional.ofNullable(workoutData.getEndTime())
            .ifPresent(existingWorkout::setEndTime);
        Optional.ofNullable(workoutData.getNotes())
            .ifPresent(existingWorkout::setNotes);
        Optional.ofNullable(workoutData.getTemplate())
            .ifPresent(existingWorkout::setTemplate);
        Optional.ofNullable(workoutData.getWorkoutExercises())
            .ifPresent(existingWorkout::setWorkoutExercises);
    }
}