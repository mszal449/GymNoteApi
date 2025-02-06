package gymnote.gymnoteapi.service;

import gymnote.gymnoteapi.entity.Template;
import gymnote.gymnoteapi.entity.Workout;
import gymnote.gymnoteapi.exception.UserNotFoundException;
import gymnote.gymnoteapi.exception.template.TemplateNotFoundException;
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
    private final TemplateService templateService;

    public List<Workout> getUserWorkouts(Long userId) {
        userService.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        return workoutRepository.findByUserId(userId);
    }

    public Workout getUserWorkoutById(Long workoutId, Long userId) {
        return workoutRepository.findByIdAndUserId(workoutId, userId)
            .orElseThrow(() -> new WorkoutNotFoundException(
                "Workout not found with id: " + workoutId + " for user: " + userId
            ));
    }


    public List<Workout> getUserTemplateWorkouts(Long templateId, Long userId) {
        return workoutRepository.findByTemplateIdAndUserId(templateId, userId);
    }

    public Workout createWorkout(Workout workout, Long templateId, Long userId) {
        try {
            Template template = templateService.getUserTemplateById(templateId, userId);
            workout.setTemplate(template);
            workout.setUser(template.getUser());

            return workoutRepository.save(workout);
        } catch (TemplateNotFoundException e) {
            throw new WorkoutCreationException("Failed to create workout due to missing template", e);
        }
        catch (DataIntegrityViolationException e) {
            throw new WorkoutCreationException("Failed to create workout due to data integrity violation", e);
        } catch (Exception e) {
            throw new WorkoutCreationException("Failed to create workout", e);
        }
    }

    public Workout updateUserWorkout(Long workoutId, Long userId, Workout workoutData) {
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
        Optional.ofNullable(workoutData.getWorkoutExercises())
            .ifPresent(existingWorkout::setWorkoutExercises);
    }
}