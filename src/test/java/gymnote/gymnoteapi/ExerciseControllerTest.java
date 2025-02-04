package gymnote.gymnoteapi;

import gymnote.gymnoteapi.controller.ExerciseController;
import gymnote.gymnoteapi.model.dto.ExerciseDTO;
import gymnote.gymnoteapi.model.exercise.ExerciseResponse;
import gymnote.gymnoteapi.model.exercise.NewExerciseRequest;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExerciseControllerTest {

    @Mock
    private ExerciseService exerciseService;

    @InjectMocks
    private ExerciseController exerciseController;

    private UserDetailsImpl userDetails;
    private ExerciseDTO exerciseDTO;
    private NewExerciseRequest newExerciseRequest;
    private NewExerciseRequest updateExerciseRequest;

    @BeforeEach
    void setUp() {
        userDetails = new UserDetailsImpl(1L, "testUser", "test@test.com", "password", null);
        exerciseDTO = new ExerciseDTO();
        exerciseDTO.setId(1L);
        exerciseDTO.setUserId(1L);
        exerciseDTO.setExerciseName("Push Up");
        exerciseDTO.setType("REPS");
        exerciseDTO.setDescription("Push up exercise");
        exerciseDTO.setOrderIndex(1);

        newExerciseRequest = new NewExerciseRequest();
        newExerciseRequest.setExerciseName("Push Up");
        newExerciseRequest.setType("REPS");
        newExerciseRequest.setDescription("Push up exercise");
        newExerciseRequest.setOrderIndex(1);


        updateExerciseRequest = new NewExerciseRequest();
        updateExerciseRequest.setExerciseName("Pull Up");
        updateExerciseRequest.setType("REPS");
        updateExerciseRequest.setDescription("Pull up exercise");
        updateExerciseRequest.setOrderIndex(2);
    }

    @Test
    void getExerciseById_Success() {
        when(exerciseService.getUserExerciseById(anyLong(), anyLong())).thenReturn(Optional.of(exerciseDTO));

        ResponseEntity<ExerciseResponse> response = exerciseController.getExerciseById(userDetails, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Push Up", response.getBody().getExercise().getExerciseName());
    }

    @Test
    void getExerciseById_NotFound() {
        when(exerciseService.getUserExerciseById(anyLong(), anyLong())).thenReturn(Optional.empty());

        ResponseEntity<ExerciseResponse> response = exerciseController.getExerciseById(userDetails, 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createExercise_Success() {
        when(exerciseService.createExercise(any(ExerciseDTO.class), anyLong())).thenReturn(Optional.of(exerciseDTO));

        ResponseEntity<ExerciseResponse> response = exerciseController.createExercise(userDetails, newExerciseRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Push Up", response.getBody().getExercise().getExerciseName());
    }

    @Test
    void createExercise_BadRequest() {
        when(exerciseService.createExercise(any(ExerciseDTO.class), anyLong())).thenReturn(Optional.empty());

        ResponseEntity<ExerciseResponse> response = exerciseController.createExercise(userDetails, newExerciseRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateExercise_Success() {
        when(exerciseService.updateExercise(anyLong(), anyLong(), any(ExerciseDTO.class))).thenReturn(Optional.of(exerciseDTO));

        ResponseEntity<ExerciseResponse> response = exerciseController.updateExercise(userDetails, 1L, updateExerciseRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Push Up", response.getBody().getExercise().getExerciseName());
    }

    @Test
    void updateExercise_NotFound() {
        when(exerciseService.updateExercise(anyLong(), anyLong(), any(ExerciseDTO.class))).thenReturn(Optional.empty());

        ResponseEntity<ExerciseResponse> response = exerciseController.updateExercise(userDetails, 1L, updateExerciseRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteExercise_Success() {
        when(exerciseService.deleteExercise(anyLong(), anyLong())).thenReturn(true);

        ResponseEntity<Void> response = exerciseController.deleteExercise(userDetails, 1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteExercise_NotFound() {
        when(exerciseService.deleteExercise(anyLong(), anyLong())).thenReturn(false);

        ResponseEntity<Void> response = exerciseController.deleteExercise(userDetails, 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}