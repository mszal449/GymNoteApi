package gymnote.gymnoteapi.serviceTest;

import gymnote.gymnoteapi.entity.EExerciseType;
import gymnote.gymnoteapi.entity.Exercise;
import gymnote.gymnoteapi.entity.User;
import gymnote.gymnoteapi.exception.UserNotFoundException;
import gymnote.gymnoteapi.exception.exercise.ExerciseCreationException;
import gymnote.gymnoteapi.exception.exercise.ExerciseDeletionException;
import gymnote.gymnoteapi.exception.exercise.ExerciseNotFoundException;
import gymnote.gymnoteapi.exception.exercise.ExerciseUpdateException;
import gymnote.gymnoteapi.repository.ExerciseRepository;
import gymnote.gymnoteapi.service.ExerciseService;
import gymnote.gymnoteapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExerciseServiceTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ExerciseService exerciseService;

    private Exercise exercise;
    private User user;
    private final Long USER_ID = 1L;
    private final Long EXERCISE_ID = 1L;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(USER_ID);

        exercise = new Exercise();
        exercise.setId(EXERCISE_ID);
        exercise.setExerciseName("Push Up");
        exercise.setType(EExerciseType.REPS);
        exercise.setDescription("Basic push up");
        exercise.setOrderIndex(1);
        exercise.setUser(user);
    }

    @Test
    void getUserExercises_Success() {
        when(exerciseRepository.findByUserId(USER_ID)).thenReturn(List.of(exercise));

        List<Exercise> result = exerciseService.getUserExercises(USER_ID);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Push Up", result.get(0).getExerciseName());
        verify(exerciseRepository).findByUserId(USER_ID);
    }

    @Test
    void getUserExerciseById_Success() {
        when(exerciseRepository.findByIdAndUserId(EXERCISE_ID, USER_ID))
                .thenReturn(Optional.of(exercise));

        Exercise result = exerciseService.getUserExerciseById(EXERCISE_ID, USER_ID);

        assertNotNull(result);
        assertEquals(EXERCISE_ID, result.getId());
        assertEquals("Push Up", result.getExerciseName());
    }

    @Test
    void getUserExerciseById_ThrowsExerciseNotFoundException() {
        when(exerciseRepository.findByIdAndUserId(EXERCISE_ID, USER_ID))
                .thenReturn(Optional.empty());

        assertThrows(ExerciseNotFoundException.class, () ->
                exerciseService.getUserExerciseById(EXERCISE_ID, USER_ID));
    }

    @Test
    void createExercise_Success() {
        when(userService.findById(USER_ID)).thenReturn(user);
        when(exerciseRepository.save(any(Exercise.class))).thenReturn(exercise);

        Exercise result = exerciseService.createExercise(exercise, USER_ID);

        assertNotNull(result);
        assertEquals("Push Up", result.getExerciseName());
        verify(exerciseRepository).save(exercise);
    }

    @Test
    void createExercise_ThrowsUserNotFoundException() {
        when(userService.findById(USER_ID)).thenThrow(new UserNotFoundException("User not found"));

        assertThrows(UserNotFoundException.class, () ->
                exerciseService.createExercise(exercise, USER_ID));
    }

    @Test
    void createExercise_ThrowsExerciseCreationException() {
        when(userService.findById(USER_ID)).thenReturn(user);
        when(exerciseRepository.save(any(Exercise.class)))
                .thenThrow(new DataIntegrityViolationException(""));

        assertThrows(ExerciseCreationException.class, () ->
                exerciseService.createExercise(exercise, USER_ID));
    }

    @Test
    void updateExercise_Success() {
        Exercise updateData = new Exercise();
        updateData.setExerciseName("Pull Up");
        updateData.setType(EExerciseType.REPS);
        updateData.setDescription("Basic pull up");
        updateData.setOrderIndex(2);

        when(exerciseRepository.findByIdAndUserId(EXERCISE_ID, USER_ID))
                .thenReturn(Optional.of(exercise));
        when(exerciseRepository.save(any(Exercise.class))).thenReturn(exercise);

        Exercise result = exerciseService.updateExercise(EXERCISE_ID, USER_ID, updateData);

        assertNotNull(result);
        verify(exerciseRepository).save(any(Exercise.class));
    }

    @Test
    void updateExercise_ThrowsExerciseNotFoundException() {
        when(exerciseRepository.findByIdAndUserId(EXERCISE_ID, USER_ID))
                .thenReturn(Optional.empty());

        assertThrows(ExerciseNotFoundException.class, () ->
                exerciseService.updateExercise(EXERCISE_ID, USER_ID, exercise));
    }

    @Test
    void updateExercise_ThrowsExerciseUpdateException() {
        when(exerciseRepository.findByIdAndUserId(EXERCISE_ID, USER_ID))
                .thenReturn(Optional.of(exercise));
        when(exerciseRepository.save(any(Exercise.class)))
                .thenThrow(new DataIntegrityViolationException(""));

        assertThrows(ExerciseUpdateException.class, () ->
                exerciseService.updateExercise(EXERCISE_ID, USER_ID, exercise));
    }

    @Test
    void deleteExercise_Success() {
        when(exerciseRepository.findByIdAndUserId(EXERCISE_ID, USER_ID))
                .thenReturn(Optional.of(exercise));
        doNothing().when(exerciseRepository).delete(exercise);

        assertDoesNotThrow(() -> exerciseService.deleteExercise(EXERCISE_ID, USER_ID));
        verify(exerciseRepository).delete(exercise);
    }

    @Test
    void deleteExercise_ThrowsExerciseNotFoundException() {
        when(exerciseRepository.findByIdAndUserId(EXERCISE_ID, USER_ID))
                .thenReturn(Optional.empty());

        assertThrows(ExerciseNotFoundException.class, () ->
                exerciseService.deleteExercise(EXERCISE_ID, USER_ID));
    }

    @Test
    void deleteExercise_ThrowsExerciseDeletionException() {
        when(exerciseRepository.findByIdAndUserId(EXERCISE_ID, USER_ID))
                .thenReturn(Optional.of(exercise));
        doThrow(new RuntimeException()).when(exerciseRepository).delete(exercise);

        assertThrows(ExerciseDeletionException.class, () ->
                exerciseService.deleteExercise(EXERCISE_ID, USER_ID));
    }
}
