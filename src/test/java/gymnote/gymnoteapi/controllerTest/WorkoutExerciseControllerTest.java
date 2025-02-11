package gymnote.gymnoteapi.controllerTest;

import gymnote.gymnoteapi.controller.WorkoutExerciseController;
import gymnote.gymnoteapi.entity.Exercise;
import gymnote.gymnoteapi.entity.User;
import gymnote.gymnoteapi.entity.Workout;
import gymnote.gymnoteapi.entity.WorkoutExercise;
import gymnote.gymnoteapi.exception.workout.WorkoutNotFoundException;
import gymnote.gymnoteapi.exception.workoutExercise.WorkoutExerciseNotFoundException;
import gymnote.gymnoteapi.model.dto.WorkoutExerciseDTO;
import gymnote.gymnoteapi.model.workoutExercise.CreateWorkoutExerciseRequest;
import gymnote.gymnoteapi.model.workoutExercise.UpdateWorkoutExerciseRequest;
import gymnote.gymnoteapi.model.workoutExercise.WorkoutExercisesResponse;
import gymnote.gymnoteapi.security.service.UserDetailsImpl;
import gymnote.gymnoteapi.service.WorkoutExerciseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class WorkoutExerciseControllerTest {

  @Mock
  private WorkoutExerciseService workoutExerciseService;

  @InjectMocks
  private WorkoutExerciseController workoutExerciseController;

  private UserDetailsImpl userDetails;
  private User user;
  private Workout workout;
  private Exercise exercise;
  private WorkoutExercise workoutExercise;
  private CreateWorkoutExerciseRequest createRequest;
  private UpdateWorkoutExerciseRequest updateRequest;

  @BeforeEach
  void setUp() {
    // Setup UserDetails
    userDetails = new UserDetailsImpl(1L, "testUser", "test@test.com", "password", Collections.emptyList());

    // Setup User
    user = new User();
    user.setId(1L);
    user.setUsername("testUser");

    // Setup Workout
    workout = new Workout();
    workout.setId(1L);
    workout.setUser(user);

    // Setup Exercise
    exercise = new Exercise();
    exercise.setId(1L);
    exercise.setUser(user);
    exercise.setExerciseName("Push Up");

    // Setup WorkoutExercise
    workoutExercise = new WorkoutExercise();
    workoutExercise.setId(1L);
    workoutExercise.setWorkout(workout);
    workoutExercise.setExercise(exercise);
    workoutExercise.setRealOrder(1);

    // Setup Create Request
    createRequest = new CreateWorkoutExerciseRequest();
    createRequest.setExerciseId(1L);
    createRequest.setRealOrder(1);

    // Setup Update Request
    updateRequest = new UpdateWorkoutExerciseRequest();
    updateRequest.setExerciseId(1L);
    updateRequest.setRealOrder(2);
  }

  @Test
  void getWorkoutExercises_Success() {
    when(workoutExerciseService.getWorkoutExercises(anyLong(), anyLong()))
      .thenReturn(List.of(workoutExercise));

    ResponseEntity<WorkoutExercisesResponse> response = 
      workoutExerciseController.getWorkoutExercises(userDetails, 1L);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().getCount());
  }

  @Test
  void getWorkoutExercises_BadRequest() {
    when(workoutExerciseService.getWorkoutExercises(anyLong(), anyLong()))
      .thenThrow(new RuntimeException());

    ResponseEntity<WorkoutExercisesResponse> response = 
      workoutExerciseController.getWorkoutExercises(userDetails, 1L);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void getWorkoutExerciseById_Success() {
    when(workoutExerciseService.getWorkoutExerciseById(anyLong(), anyLong(), anyLong()))
      .thenReturn(workoutExercise);

    ResponseEntity<WorkoutExerciseDTO> response = 
      workoutExerciseController.getWorkoutExerciseById(userDetails, 1L, 1L);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1L, response.getBody().getId());
  }

  @Test
  void getWorkoutExerciseById_NotFound() {
    when(workoutExerciseService.getWorkoutExerciseById(anyLong(), anyLong(), anyLong()))
      .thenThrow(new WorkoutExerciseNotFoundException("Not found"));

    ResponseEntity<WorkoutExerciseDTO> response = 
      workoutExerciseController.getWorkoutExerciseById(userDetails, 1L, 1L);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void createWorkoutExercise_Success() {
    when(workoutExerciseService.createWorkoutExercise(any(), anyLong(), anyLong()))
      .thenReturn(workoutExercise);

    ResponseEntity<WorkoutExerciseDTO> response = 
      workoutExerciseController.createWorkoutExercise(userDetails, 1L, createRequest);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1L, response.getBody().getId());
  }

  @Test
  void createWorkoutExercise_BadRequest() {
    when(workoutExerciseService.createWorkoutExercise(any(), anyLong(), anyLong()))
      .thenThrow(new RuntimeException());

    ResponseEntity<WorkoutExerciseDTO> response = 
      workoutExerciseController.createWorkoutExercise(userDetails, 1L, createRequest);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void updateWorkoutExercise_Success() {
    when(workoutExerciseService.updateWorkoutExercise(anyLong(), anyLong(), anyLong(), any()))
      .thenReturn(workoutExercise);

    ResponseEntity<WorkoutExerciseDTO> response = 
      workoutExerciseController.updateWorkoutExercise(userDetails, 1L, 1L, updateRequest);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1L, response.getBody().getId());
  }

  @Test
  void updateWorkoutExercise_NotFound() {
    when(workoutExerciseService.updateWorkoutExercise(anyLong(), anyLong(), anyLong(), any()))
      .thenThrow(new WorkoutExerciseNotFoundException("Not found"));

    ResponseEntity<WorkoutExerciseDTO> response = 
      workoutExerciseController.updateWorkoutExercise(userDetails, 1L, 1L, updateRequest);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void deleteWorkoutExercise_Success() {
    doNothing().when(workoutExerciseService)
      .deleteWorkoutExercise(anyLong(), anyLong(), anyLong());

    ResponseEntity<?> response = 
      workoutExerciseController.deleteWorkoutExercise(userDetails, 1L, 1L);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(workoutExerciseService).deleteWorkoutExercise(1L, 1L, 1L);
  }

  @Test
  void deleteWorkoutExercise_NotFound() {
    doThrow(new WorkoutNotFoundException("Not found"))
      .when(workoutExerciseService)
      .deleteWorkoutExercise(anyLong(), anyLong(), anyLong());

    ResponseEntity<?> response = 
      workoutExerciseController.deleteWorkoutExercise(userDetails, 1L, 1L);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }
}
