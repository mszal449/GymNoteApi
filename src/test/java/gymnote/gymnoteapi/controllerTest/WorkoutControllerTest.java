package gymnote.gymnoteapi.controllerTest;

import gymnote.gymnoteapi.controller.WorkoutController;
import gymnote.gymnoteapi.entity.User;
import gymnote.gymnoteapi.entity.Workout;
import gymnote.gymnoteapi.exception.workout.WorkoutCreationException;
import gymnote.gymnoteapi.exception.workout.WorkoutNotFoundException;
import gymnote.gymnoteapi.model.dto.WorkoutDTO;
import gymnote.gymnoteapi.model.workout.CreateWorkoutRequest;
import gymnote.gymnoteapi.model.workout.UpdateWorkoutRequest;
import gymnote.gymnoteapi.model.workout.WorkoutsResponse;
import gymnote.gymnoteapi.security.service.UserDetailsImpl;
import gymnote.gymnoteapi.service.WorkoutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutControllerTest {

    @Mock
    private WorkoutService workoutService;

    @InjectMocks
    private WorkoutController workoutController;

    private UserDetailsImpl userDetails;
    private User user;
    private Workout workout;
    private CreateWorkoutRequest createWorkoutRequest;
    private UpdateWorkoutRequest updateWorkoutRequest;

    @BeforeEach
    void setUp() {
        userDetails = new UserDetailsImpl(1L, "testUser", "test@test.com", "password", Collections.emptyList());

        user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        workout = new Workout();
        workout.setId(1L);
        workout.setUser(user);
        workout.setName("Test Workout");
        workout.setStartTime(LocalDateTime.now());

        createWorkoutRequest = new CreateWorkoutRequest();
        createWorkoutRequest.setName("Test Workout");
        createWorkoutRequest.setStartTime(LocalDateTime.now());
        createWorkoutRequest.setTemplateId(1L);

        updateWorkoutRequest = new UpdateWorkoutRequest();
        updateWorkoutRequest.setName("Updated Workout");
        updateWorkoutRequest.setStartTime(LocalDateTime.now());
    }

    @Test
    void getUserWorkouts_Success() {
        when(workoutService.getUserWorkouts(anyLong())).thenReturn(List.of(workout));

        ResponseEntity<WorkoutsResponse> response = workoutController.getUserWorkouts(userDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getCount());
        assertEquals("Test Workout", response.getBody().getWorkouts().get(0).getName());
    }

    @Test
    void getUserWorkoutById_Success() {
        when(workoutService.getUserWorkoutById(anyLong(), anyLong())).thenReturn(workout);

        ResponseEntity<WorkoutDTO> response = workoutController.getUserWorkoutById(userDetails, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Workout", response.getBody().getName());
    }

    @Test
    void getUserWorkoutById_NotFound() {
        when(workoutService.getUserWorkoutById(anyLong(), anyLong()))
                .thenThrow(new WorkoutNotFoundException("Workout not found"));

        ResponseEntity<WorkoutDTO> response = workoutController.getUserWorkoutById(userDetails, 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getTemplateWorkouts_Success() {
        when(workoutService.getUserTemplateWorkouts(anyLong(), anyLong())).thenReturn(List.of(workout));

        ResponseEntity<WorkoutsResponse> response = workoutController.getTemplateWorkouts(userDetails, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getCount());
        assertEquals("Test Workout", response.getBody().getWorkouts().get(0).getName());
    }

    @Test
    void getTemplateWorkouts_BadRequest() {
        when(workoutService.getUserTemplateWorkouts(anyLong(), anyLong())).thenThrow(new RuntimeException());

        ResponseEntity<WorkoutsResponse> response = workoutController.getTemplateWorkouts(userDetails, 1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    void createWorkout_Success() {
        when(workoutService.createWorkout(any(Workout.class), anyLong(), anyLong())).thenReturn(workout);

        ResponseEntity<WorkoutDTO> response = workoutController.createWorkout(userDetails, createWorkoutRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Workout", response.getBody().getName());
    }


    @Test
    void createWorkout_BadRequest() {
        when(workoutService.createWorkout(any(Workout.class), anyLong(), anyLong()))
                .thenThrow(new WorkoutCreationException("Failed to create workout", new RuntimeException()));

        ResponseEntity<WorkoutDTO> response = workoutController.createWorkout(userDetails, createWorkoutRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateWorkout_Success() {
        when(workoutService.updateUserWorkout(anyLong(), anyLong(), any(Workout.class))).thenReturn(workout);

        ResponseEntity<WorkoutDTO> response = workoutController.updateWorkout(userDetails, 1L, updateWorkoutRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Workout", response.getBody().getName());
    }

    @Test
    void updateWorkout_NotFound() {
        when(workoutService.updateUserWorkout(anyLong(), anyLong(), any(Workout.class)))
                .thenThrow(new WorkoutNotFoundException("Workout not found"));

        ResponseEntity<WorkoutDTO> response = workoutController.updateWorkout(userDetails, 1L, updateWorkoutRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteWorkout_Success() {
        doNothing().when(workoutService).deleteWorkout(anyLong(), anyLong());

        ResponseEntity<?> response = workoutController.deleteWorkout(userDetails, 1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(workoutService).deleteWorkout(1L, 1L);
    }

    @Test
    void deleteWorkout_NotFound() {
        doThrow(new WorkoutNotFoundException("Workout not found"))
                .when(workoutService).deleteWorkout(anyLong(), anyLong());

        ResponseEntity<?> response = workoutController.deleteWorkout(userDetails, 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}