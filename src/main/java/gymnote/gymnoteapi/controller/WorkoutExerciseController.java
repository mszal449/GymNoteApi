package gymnote.gymnoteapi.controller;

import gymnote.gymnoteapi.entity.WorkoutExercise;
import gymnote.gymnoteapi.exception.workout.WorkoutNotFoundException;
import gymnote.gymnoteapi.exception.workoutExercise.WorkoutExerciseNotFoundException;
import gymnote.gymnoteapi.mapper.WorkoutExerciseMapper;
import gymnote.gymnoteapi.model.api.ApiResponse;
import gymnote.gymnoteapi.model.dto.WorkoutExerciseDTO;
import gymnote.gymnoteapi.model.workoutExercise.CreateWorkoutExerciseRequest;
import gymnote.gymnoteapi.model.workoutExercise.UpdateWorkoutExerciseRequest;
import gymnote.gymnoteapi.security.service.UserDetailsImpl;
import gymnote.gymnoteapi.service.WorkoutExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
@RequestMapping("/api/workout/{workoutId}/exercise")
public class WorkoutExerciseController {
    private final WorkoutExerciseService workoutExerciseService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<WorkoutExerciseDTO>>> getWorkoutExercises(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long workoutId) {
        try {
            List<WorkoutExercise> exercises = workoutExerciseService.getWorkoutExercises(workoutId, userDetails.getId());
            List<WorkoutExerciseDTO> exerciseDTOs = exercises.stream()
                .map(WorkoutExerciseMapper::toDTO)
                .toList();
            return ResponseEntity.ok(ApiResponse.success(exerciseDTOs));
        } catch (WorkoutNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Workout not found"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to fetch workout exercises"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkoutExerciseDTO>> getWorkoutExerciseById(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long workoutId,
            @PathVariable Long id) {
        try {
            WorkoutExercise exercise = workoutExerciseService.getWorkoutExerciseById(id, workoutId, userDetails.getId());
            return ResponseEntity.ok(ApiResponse.success(WorkoutExerciseMapper.toDTO(exercise)));
        } catch (WorkoutExerciseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Workout exercise not found"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to fetch workout exercise"));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<WorkoutExerciseDTO>> createWorkoutExercise(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long workoutId,
            @RequestBody CreateWorkoutExerciseRequest createRequest) {
        try {
            WorkoutExercise exercise = WorkoutExerciseMapper.toEntity(createRequest);
            WorkoutExercise saved = workoutExerciseService.createWorkoutExercise(
                exercise,
                workoutId,
                createRequest.getExerciseId(),
                userDetails.getId()
            );
            return ResponseEntity.ok(ApiResponse.success(WorkoutExerciseMapper.toDTO(saved)));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to create workout exercise: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkoutExerciseDTO>> updateWorkoutExercise(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long workoutId,
            @PathVariable Long id,
            @RequestBody UpdateWorkoutExerciseRequest updateRequest) {
        try {
            WorkoutExercise exerciseData = WorkoutExerciseMapper.toEntity(updateRequest);
            WorkoutExercise updated = workoutExerciseService.updateWorkoutExercise(
                id, workoutId, userDetails.getId(), exerciseData
            );
            return ResponseEntity.ok(ApiResponse.success(WorkoutExerciseMapper.toDTO(updated)));
        } catch (WorkoutExerciseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Workout exercise not found"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to update workout exercise"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteWorkoutExercise(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long workoutId,
            @PathVariable Long id) {
        try {
            workoutExerciseService.deleteWorkoutExercise(id, workoutId, userDetails.getId());
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (WorkoutNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Workout not found"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to delete workout exercise"));
        }
    }
}