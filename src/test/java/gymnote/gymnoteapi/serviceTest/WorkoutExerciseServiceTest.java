package gymnote.gymnoteapi.serviceTest;

import gymnote.gymnoteapi.entity.*;
import gymnote.gymnoteapi.exception.workout.WorkoutNotFoundException;
import gymnote.gymnoteapi.exception.workoutExercise.WorkoutExerciseCreationException;
import gymnote.gymnoteapi.exception.workoutExercise.WorkoutExerciseNotFoundException;
import gymnote.gymnoteapi.repository.WorkoutExerciseRepository;
import gymnote.gymnoteapi.repository.WorkoutRepository;
import gymnote.gymnoteapi.service.ExerciseService;
import gymnote.gymnoteapi.service.WorkoutExerciseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WorkoutExerciseServiceTest {

    @Mock
    private WorkoutExerciseRepository workoutExerciseRepository;

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private ExerciseService exerciseService;

    @InjectMocks
    private WorkoutExerciseService workoutExerciseService;

    private User user;
    private Workout workout;
    private Exercise exercise;
    private WorkoutExercise workoutExercise;
    private final Long USER_ID = 1L;
    private final Long WORKOUT_ID = 1L;
    private final Long EXERCISE_ID = 1L;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(USER_ID);

        workout = new Workout();
        workout.setId(WORKOUT_ID);
        workout.setUser(user);
        workout.setName("Test Workout");
        workout.setStartTime(LocalDateTime.now());

        exercise = new Exercise();
        exercise.setId(EXERCISE_ID);
        exercise.setExerciseName("Push Up");
        exercise.setType(EExerciseType.REPS);
        exercise.setUser(user);

        workoutExercise = new WorkoutExercise();
        workoutExercise.setId(1L);
        workoutExercise.setWorkout(workout);
        workoutExercise.setExercise(exercise);
        workoutExercise.setExerciseOrder(1);
    }

    @Test
    void getWorkoutExercises_Success() {
        when(workoutRepository.findByIdAndUserId(WORKOUT_ID, USER_ID))
                .thenReturn(Optional.of(workout));
        when(workoutExerciseRepository.findByWorkoutIdOrderByExerciseOrder(WORKOUT_ID))
                .thenReturn(List.of(workoutExercise));

        List<WorkoutExercise> result = workoutExerciseService.getWorkoutExercises(WORKOUT_ID, USER_ID);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(workoutExercise.getId(), result.get(0).getId());
    }

    @Test
    void getWorkoutExercises_ThrowsWorkoutNotFoundException() {
        when(workoutRepository.findByIdAndUserId(WORKOUT_ID, USER_ID))
                .thenReturn(Optional.empty());

        assertThrows(WorkoutNotFoundException.class, () ->
                workoutExerciseService.getWorkoutExercises(WORKOUT_ID, USER_ID));
    }

    @Test
    void getWorkoutExerciseById_Success() {
        when(workoutRepository.findByIdAndUserId(WORKOUT_ID, USER_ID))
                .thenReturn(Optional.of(workout));
        when(workoutExerciseRepository.findByIdAndWorkoutId(EXERCISE_ID, WORKOUT_ID))
                .thenReturn(Optional.of(workoutExercise));

        WorkoutExercise result = workoutExerciseService
                .getWorkoutExerciseById(EXERCISE_ID, WORKOUT_ID, USER_ID);

        assertNotNull(result);
        assertEquals(workoutExercise.getId(), result.getId());
    }

    @Test
    void getWorkoutExerciseById_ThrowsWorkoutExerciseNotFoundException() {
        when(workoutRepository.findByIdAndUserId(WORKOUT_ID, USER_ID))
                .thenReturn(Optional.of(workout));
        when(workoutExerciseRepository.findByIdAndWorkoutId(EXERCISE_ID, WORKOUT_ID))
                .thenReturn(Optional.empty());

        assertThrows(WorkoutExerciseNotFoundException.class, () ->
                workoutExerciseService.getWorkoutExerciseById(EXERCISE_ID, WORKOUT_ID, USER_ID));
    }

    @Test
    void createWorkoutExercise_Success() {
        when(workoutRepository.findByIdAndUserId(WORKOUT_ID, USER_ID))
                .thenReturn(Optional.of(workout));
        when(exerciseService.getUserExerciseById(EXERCISE_ID, USER_ID))
                .thenReturn(exercise);
        when(workoutExerciseRepository.save(any(WorkoutExercise.class)))
                .thenReturn(workoutExercise);

        WorkoutExercise result = workoutExerciseService
                .createWorkoutExercise(workoutExercise, WORKOUT_ID, EXERCISE_ID, USER_ID);

        assertNotNull(result);
        assertEquals(workoutExercise.getId(), result.getId());
        verify(workoutExerciseRepository).save(any(WorkoutExercise.class));
    }

    @Test
    void createWorkoutExercise_ThrowsWorkoutExerciseCreationException() {
        when(workoutRepository.findByIdAndUserId(WORKOUT_ID, USER_ID))
                .thenReturn(Optional.of(workout));
        when(exerciseService.getUserExerciseById(EXERCISE_ID, USER_ID))
                .thenReturn(exercise);
        when(workoutExerciseRepository.save(any(WorkoutExercise.class)))
                .thenThrow(new DataIntegrityViolationException(""));

        assertThrows(WorkoutExerciseCreationException.class, () ->
                workoutExerciseService.createWorkoutExercise(workoutExercise, WORKOUT_ID, EXERCISE_ID, USER_ID));
    }
}