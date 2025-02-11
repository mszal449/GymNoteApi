package gymnote.gymnoteapi.serviceTest;

import gymnote.gymnoteapi.entity.Template;
import gymnote.gymnoteapi.entity.User;
import gymnote.gymnoteapi.entity.Workout;
import gymnote.gymnoteapi.exception.UserNotFoundException;
import gymnote.gymnoteapi.exception.workout.WorkoutCreationException;
import gymnote.gymnoteapi.exception.workout.WorkoutNotFoundException;
import gymnote.gymnoteapi.repository.WorkoutRepository;
import gymnote.gymnoteapi.service.TemplateService;
import gymnote.gymnoteapi.service.UserService;
import gymnote.gymnoteapi.service.WorkoutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutServiceTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private UserService userService;

    @Mock
    private TemplateService templateService;

    @InjectMocks
    private WorkoutService workoutService;

    private User user;
    private Workout workout;
    private Template template;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        workout = new Workout();
        workout.setId(1L);
        workout.setUser(user);
        workout.setName("Test Workout");
        workout.setStartTime(LocalDateTime.now());

        template = new Template();
        template.setId(1L);
        template.setUser(user);
    }

    @Test
    void getUserWorkouts_Success() {
        when(userService.findById(1L)).thenReturn(user);
        when(workoutRepository.findByUserId(1L)).thenReturn(List.of(workout));

        List<Workout> result = workoutService.getUserWorkouts(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Workout", result.get(0).getName());
        verify(workoutRepository).findByUserId(1L);
    }

    @Test
    void getUserWorkouts_UserNotFound() {
        when(userService.findById(1L)).thenThrow(new UserNotFoundException(""));

        assertThrows(UserNotFoundException.class, () -> {
            workoutService.getUserWorkouts(1L);
        });
    }

    @Test
    void getUserWorkoutById_Success() {
        when(workoutRepository.findByIdAndUserIdWithExercises(1L, 1L))
                .thenReturn(Optional.of(workout));

        Workout result = workoutService.getUserWorkoutById(1L, 1L);

        assertNotNull(result);
        assertEquals("Test Workout", result.getName());
        verify(workoutRepository).findByIdAndUserIdWithExercises(1L, 1L);
    }

    @Test
    void getUserWorkoutById_NotFound() {
        when(workoutRepository.findByIdAndUserIdWithExercises(1L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(WorkoutNotFoundException.class, () -> {
            workoutService.getUserWorkoutById(1L, 1L);
        });
    }

    @Test
    void createWorkout_Success_WithTemplate() {
        // Mock finding the template
        when(templateService.getUserTemplateById(1L, 1L)).thenReturn(template);

        // Mock saving the workout
        when(workoutRepository.save(any(Workout.class))).thenReturn(workout);

        // Create a workout with a template
        Workout workoutWithTemplate = new Workout();
        workoutWithTemplate.setName("Test Workout");
        workoutWithTemplate.setStartTime(LocalDateTime.now());
        workoutWithTemplate.setTemplate(template);


        Workout result = workoutService.createWorkout(workoutWithTemplate, 1L, 1L);

        assertNotNull(result);
        assertEquals("Test Workout", result.getName());
        assertEquals(user, result.getUser());
        assertEquals(template.getId(), result.getId());
        verify(workoutRepository).save(any(Workout.class));
    }


    @Test
    void createWorkout_UserNotFound() {
        assertThrows(WorkoutCreationException.class, () -> {
            workoutService.createWorkout(workout, 1L, 1L);
        });
    }

    @Test
    void createWorkout_DataIntegrityViolation() {

        assertThrows(WorkoutCreationException.class, () -> {
            workoutService.createWorkout(workout, 1L, 1L);
        });
    }

    @Test
    void updateUserWorkout_Success() {
        Workout updatedWorkout = new Workout();
        updatedWorkout.setName("Updated Workout");

        when(workoutRepository.findByIdAndUserId(1L, 1L))
                .thenReturn(Optional.of(workout));
        when(workoutRepository.save(any(Workout.class)))
                .thenReturn(updatedWorkout);

        Workout result = workoutService.updateUserWorkout(1L, 1L, updatedWorkout);

        assertNotNull(result);
        assertEquals("Updated Workout", result.getName());
        verify(workoutRepository).save(any(Workout.class));
    }

    @Test
    void updateUserWorkout_NotFound() {
        when(workoutRepository.findByIdAndUserId(1L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(WorkoutNotFoundException.class, () -> {
            workoutService.updateUserWorkout(1L, 1L, workout);
        });
    }

    @Test
    void deleteWorkout_Success() {
        when(workoutRepository.findByIdAndUserId(1L, 1L))
                .thenReturn(Optional.of(workout));
        doNothing().when(workoutRepository).delete(any(Workout.class));

        assertDoesNotThrow(() -> {
            workoutService.deleteWorkout(1L, 1L);
        });

        verify(workoutRepository).delete(workout);
    }

    @Test
    void deleteWorkout_NotFound() {
        when(workoutRepository.findByIdAndUserId(1L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(WorkoutNotFoundException.class, () -> {
            workoutService.deleteWorkout(1L, 1L);
        });
    }
}