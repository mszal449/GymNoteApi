package gymnote.gymnoteapi.serviceTest;

import gymnote.gymnoteapi.entity.*;
import gymnote.gymnoteapi.exception.exerciseSet.ExerciseSetCreationException;
import gymnote.gymnoteapi.exception.exerciseSet.ExerciseSetDeletionException;
import gymnote.gymnoteapi.exception.exerciseSet.ExerciseSetNotFoundException;
import gymnote.gymnoteapi.exception.exerciseSet.ExerciseSetUpdateException;
import gymnote.gymnoteapi.repository.ExerciseSetRepository;
import gymnote.gymnoteapi.service.ExerciseSetService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExerciseSetServiceTest {

    @Mock
    private ExerciseSetRepository exerciseSetRepository;

    @Mock
    private WorkoutExerciseService workoutExerciseService;

    @InjectMocks
    private ExerciseSetService exerciseSetService;

    private User user;
    private Workout workout;
    private Exercise exercise;
    private WorkoutExercise workoutExercise;
    private ExerciseSet exerciseSet;
    private final Long USER_ID = 1L;
    private final Long WORKOUT_ID = 1L;
    private final Long EXERCISE_ID = 1L;
    private final Long SET_ID = 1L;

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

        workoutExercise = new WorkoutExercise();
        workoutExercise.setId(EXERCISE_ID);
        workoutExercise.setWorkout(workout);
        workoutExercise.setExercise(exercise);

        exerciseSet = new ExerciseSet();
        exerciseSet.setId(SET_ID);
        exerciseSet.setWorkoutExercise(workoutExercise);
        exerciseSet.setReps(10);
        exerciseSet.setWeight(20.0);
        exerciseSet.setSetOrder(1);
    }

    @Test
    void getExerciseSets_Success() {
        when(exerciseSetRepository.findAllValidated(EXERCISE_ID, WORKOUT_ID, USER_ID))
                .thenReturn(List.of(exerciseSet));

        List<ExerciseSet> result = exerciseSetService.getExerciseSets(WORKOUT_ID, EXERCISE_ID, USER_ID);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(exerciseSet.getId(), result.get(0).getId());
    }

    @Test
    void getExerciseSetById_Success() {
        when(exerciseSetRepository.findOneValidated(SET_ID, EXERCISE_ID, WORKOUT_ID, USER_ID))
                .thenReturn(Optional.of(exerciseSet));

        ExerciseSet result = exerciseSetService.getExerciseSetById(SET_ID, EXERCISE_ID, WORKOUT_ID, USER_ID);

        assertNotNull(result);
        assertEquals(SET_ID, result.getId());
    }

    @Test
    void getExerciseSetById_ThrowsNotFoundException() {
        when(exerciseSetRepository.findOneValidated(SET_ID, EXERCISE_ID, WORKOUT_ID, USER_ID))
                .thenReturn(Optional.empty());

        assertThrows(ExerciseSetNotFoundException.class, () ->
                exerciseSetService.getExerciseSetById(SET_ID, EXERCISE_ID, WORKOUT_ID, USER_ID));
    }

    @Test
    void createExerciseSet_Success() {
        when(workoutExerciseService.getWorkoutExerciseById(EXERCISE_ID, WORKOUT_ID, USER_ID))
                .thenReturn(workoutExercise);
        when(exerciseSetRepository.save(any(ExerciseSet.class)))
                .thenReturn(exerciseSet);

        ExerciseSet result = exerciseSetService
                .createExerciseSet(exerciseSet, EXERCISE_ID, WORKOUT_ID, USER_ID);

        assertNotNull(result);
        assertEquals(SET_ID, result.getId());
        verify(exerciseSetRepository).save(any(ExerciseSet.class));
    }

    @Test
    void createExerciseSet_ThrowsCreationException() {
        when(workoutExerciseService.getWorkoutExerciseById(EXERCISE_ID, WORKOUT_ID, USER_ID))
                .thenReturn(workoutExercise);
        when(exerciseSetRepository.save(any(ExerciseSet.class)))
                .thenThrow(new DataIntegrityViolationException(""));

        assertThrows(ExerciseSetCreationException.class, () ->
                exerciseSetService.createExerciseSet(exerciseSet, EXERCISE_ID, WORKOUT_ID, USER_ID));
    }

    @Test
    void updateExerciseSet_Success() {
        ExerciseSet updateData = new ExerciseSet();
        updateData.setReps(12);
        updateData.setWeight(25.0);

        when(exerciseSetRepository.findOneValidated(SET_ID, EXERCISE_ID, WORKOUT_ID, USER_ID))
                .thenReturn(Optional.of(exerciseSet));
        when(exerciseSetRepository.save(any(ExerciseSet.class)))
                .thenReturn(exerciseSet);

        ExerciseSet result = exerciseSetService
                .updateExerciseSet(SET_ID, updateData, EXERCISE_ID, WORKOUT_ID, USER_ID);

        assertNotNull(result);
        verify(exerciseSetRepository).save(any(ExerciseSet.class));
    }

    @Test
    void updateExerciseSet_ThrowsUpdateException() {
        when(exerciseSetRepository.findOneValidated(SET_ID, EXERCISE_ID, WORKOUT_ID, USER_ID))
                .thenReturn(Optional.of(exerciseSet));
        when(exerciseSetRepository.save(any(ExerciseSet.class)))
                .thenThrow(new DataIntegrityViolationException(""));

        assertThrows(ExerciseSetUpdateException.class, () ->
                exerciseSetService.updateExerciseSet(SET_ID, exerciseSet, EXERCISE_ID, WORKOUT_ID, USER_ID));
    }

    @Test
    void deleteExerciseSet_Success() {
        when(exerciseSetRepository.findOneValidated(SET_ID, EXERCISE_ID, WORKOUT_ID, USER_ID))
                .thenReturn(Optional.of(exerciseSet));
        when(exerciseSetRepository.deleteValidated(SET_ID, EXERCISE_ID, WORKOUT_ID, USER_ID))
                .thenReturn(1);

        assertDoesNotThrow(() ->
                exerciseSetService.deleteExerciseSet(SET_ID, EXERCISE_ID, WORKOUT_ID, USER_ID));

        verify(exerciseSetRepository).deleteValidated(SET_ID, EXERCISE_ID, WORKOUT_ID, USER_ID);
        verify(exerciseSetRepository).reorderAfterDelete(anyLong(), anyLong(), anyLong(), anyInt());
    }

    @Test
    void deleteExerciseSet_ThrowsDeletionException() {
        when(exerciseSetRepository.findOneValidated(SET_ID, EXERCISE_ID, WORKOUT_ID, USER_ID))
                .thenReturn(Optional.of(exerciseSet));
        when(exerciseSetRepository.deleteValidated(SET_ID, EXERCISE_ID, WORKOUT_ID, USER_ID))
                .thenThrow(new RuntimeException());

        assertThrows(ExerciseSetDeletionException.class, () ->
                exerciseSetService.deleteExerciseSet(SET_ID, EXERCISE_ID, WORKOUT_ID, USER_ID));
    }
}
