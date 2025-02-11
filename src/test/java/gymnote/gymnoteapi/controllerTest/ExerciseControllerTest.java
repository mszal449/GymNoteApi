package gymnote.gymnoteapi.controllerTest;

import gymnote.gymnoteapi.controller.ExerciseController;
import gymnote.gymnoteapi.entity.EExerciseType;
import gymnote.gymnoteapi.entity.Exercise;
import gymnote.gymnoteapi.entity.User;
import gymnote.gymnoteapi.exception.exercise.ExerciseCreationException;
import gymnote.gymnoteapi.exception.exercise.ExerciseNotFoundException;
import gymnote.gymnoteapi.model.api.ApiResponse;
import gymnote.gymnoteapi.model.dto.ExerciseDTO;
import gymnote.gymnoteapi.model.exercise.CreateExerciseRequest;
import gymnote.gymnoteapi.model.exercise.UpdateExerciseRequest;
import gymnote.gymnoteapi.security.service.UserDetailsImpl;
import gymnote.gymnoteapi.service.ExerciseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExerciseControllerTest {

    @Mock
    private ExerciseService exerciseService;

    @InjectMocks
    private ExerciseController exerciseController;

    private UserDetailsImpl userDetails;
    private User user;
    private Exercise exercise;
    private CreateExerciseRequest createExerciseRequest;
    private UpdateExerciseRequest updateExerciseRequest;

    @BeforeEach
    void setUp() {
        userDetails = new UserDetailsImpl(1L, "testUser", "test@test.com", "password", null);
        User user = new User();
        user.setId(userDetails.getId());
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());

        exercise = new Exercise();
        exercise.setId(1L);
        exercise.setUser(user);
        exercise.setExerciseName("Push Up");
        exercise.setType(EExerciseType.REPS);
        exercise.setDescription("Push up exercise");
        exercise.setOrderIndex(1);

        createExerciseRequest = new CreateExerciseRequest();
        createExerciseRequest.setExerciseName("Push Up");
        createExerciseRequest.setType("REPS");
        createExerciseRequest.setDescription("Push up exercise");
        createExerciseRequest.setOrderIndex(1);


        updateExerciseRequest = new UpdateExerciseRequest();
        updateExerciseRequest.setExerciseName("Pull Up");
        updateExerciseRequest.setType("REPS");
        updateExerciseRequest.setDescription("Pull up exercise");
        updateExerciseRequest.setOrderIndex(2);
    }

    @Test
    void getExerciseById_Success() {
        when(exerciseService.getUserExerciseById(anyLong(), anyLong())).thenReturn(exercise);

        ResponseEntity<ApiResponse<ExerciseDTO>> response = exerciseController.getExerciseById(userDetails, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertEquals("Push Up", response.getBody().getData().getExerciseName());
        assertEquals("Success", response.getBody().getMessage());
    }

    @Test
    void getExerciseById_NotFound() {
        when(exerciseService.getUserExerciseById(anyLong(), anyLong()))
                .thenThrow(new ExerciseNotFoundException("Exercise not found"));

        ResponseEntity<ApiResponse<ExerciseDTO>> response = exerciseController.getExerciseById(userDetails, 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Exercise not found", response.getBody().getMessage());
    }

    @Test
    void createExercise_Success() {
        when(exerciseService.createExercise(any(Exercise.class), anyLong())).thenReturn(exercise);

        ResponseEntity<ApiResponse<ExerciseDTO>> response = exerciseController.createExercise(userDetails, createExerciseRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertEquals("Push Up", response.getBody().getData().getExerciseName());
        assertEquals("Success", response.getBody().getMessage());
    }

    @Test
    void createExercise_BadRequest() {
        when(exerciseService.createExercise(any(Exercise.class), anyLong())).thenThrow(new
                ExerciseCreationException("Failed to create exercise", new Exception()));

        ResponseEntity<ApiResponse<ExerciseDTO>> response = exerciseController.createExercise(userDetails, createExerciseRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Failed to create exercise", response.getBody().getMessage());
    }

    @Test
    void updateExercise_Success() {
        when(exerciseService.updateExercise(anyLong(), anyLong(), any(Exercise.class))).thenReturn(exercise);

        ResponseEntity<ApiResponse<ExerciseDTO>> response =
                exerciseController.updateExercise(userDetails, 1L, updateExerciseRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertEquals("Push Up", response.getBody().getData().getExerciseName());
        assertEquals("Success", response.getBody().getMessage());
    }

    @Test
    void updateExercise_NotFound() {
        when(exerciseService.updateExercise(anyLong(), anyLong(), any(Exercise.class)))
                .thenThrow(new ExerciseNotFoundException("Exercise not found"));

        ResponseEntity<ApiResponse<ExerciseDTO>> response = exerciseController
                .updateExercise(userDetails, 1L, updateExerciseRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Exercise not found", response.getBody().getMessage());
    }

    @Test
    void deleteExercise_Success() {
        doNothing().when(exerciseService).deleteExercise(anyLong(), anyLong());

        ResponseEntity<ApiResponse<Void>> response = exerciseController.deleteExercise(userDetails, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getMessage());
    }

    @Test
    void deleteExercise_NotFound() {
        doThrow(new ExerciseNotFoundException("Exercise not found"))
            .when(exerciseService).deleteExercise(anyLong(), anyLong());

        ResponseEntity<ApiResponse<Void>> response = exerciseController.deleteExercise(userDetails, 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Exercise not found", response.getBody().getMessage());
    }
}