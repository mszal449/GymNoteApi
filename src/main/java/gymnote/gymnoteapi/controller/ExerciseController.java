package gymnote.gymnoteapi.controller;

import gymnote.gymnoteapi.entity.Exercise;
import gymnote.gymnoteapi.exception.UserNotFoundException;
import gymnote.gymnoteapi.exception.exercise.ExerciseCreationException;
import gymnote.gymnoteapi.exception.exercise.ExerciseNotFoundException;
import gymnote.gymnoteapi.model.api.ApiResponse;
import gymnote.gymnoteapi.model.dto.ExerciseDTO;
import gymnote.gymnoteapi.model.exercise.CreateExerciseRequest;
import gymnote.gymnoteapi.model.exercise.UpdateExerciseRequest;
import gymnote.gymnoteapi.security.service.UserDetailsImpl;
import gymnote.gymnoteapi.service.ExerciseService;
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
@RequestMapping("/api/exercise")
public class ExerciseController {
    private final ExerciseService exerciseService;

    @GetMapping()
    public ResponseEntity<ApiResponse<List<ExerciseDTO>>> getExercises(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            List<Exercise> exercises = exerciseService.getUserExercises(userDetails.getId());
            List<ExerciseDTO> exerciseDTOs = exercises.stream()
                    .map(Exercise::toDTO)
                    .toList();
            return ResponseEntity.ok(ApiResponse.success(exerciseDTOs));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to fetch exercises"));
        }
    }

    @GetMapping("/{exerciseId}")
    public ResponseEntity<ApiResponse<ExerciseDTO>> getExerciseById(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long exerciseId) {
        try {
            Exercise exercise = exerciseService.getUserExerciseById(exerciseId, userDetails.getId());
            return ResponseEntity.ok(ApiResponse.success(exercise.toDTO()));
        } catch (ExerciseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Exercise not found"));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ExerciseDTO>> createExercise(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CreateExerciseRequest request) {
        try {
            Exercise exercise = request.toEntity();
            Exercise saved = exerciseService.createExercise(exercise, userDetails.getId());
            return ResponseEntity.ok(ApiResponse.success(saved.toDTO()));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("User not found"));
        } catch (ExerciseCreationException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to create exercise"));
        }
    }

    @PutMapping("/{exerciseId}")
    public ResponseEntity<ApiResponse<ExerciseDTO>> updateExercise(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long exerciseId,
            @RequestBody UpdateExerciseRequest updateExerciseRequest) {
        try {
            Exercise exerciseData = updateExerciseRequest.toEntity();
            Exercise updated = exerciseService.updateExercise(exerciseId, userDetails.getId(), exerciseData);
            return ResponseEntity.ok(ApiResponse.success(updated.toDTO()));
        } catch (ExerciseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Exercise not found"));
        } catch (ExerciseCreationException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to update exercise"));
        }
    }

    @DeleteMapping("/{exerciseId}")
    public ResponseEntity<ApiResponse<Void>> deleteExercise(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long exerciseId) {
        try {
            exerciseService.deleteExercise(exerciseId, userDetails.getId());
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (ExerciseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Exercise not found"));
        }
    }
}