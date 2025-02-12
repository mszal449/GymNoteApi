package gymnote.gymnoteapi.controller;

import gymnote.gymnoteapi.entity.Exercise;
import gymnote.gymnoteapi.model.api.ApiResponse;
import gymnote.gymnoteapi.model.dto.ExerciseDTO;
import gymnote.gymnoteapi.model.exercise.CreateExerciseRequest;
import gymnote.gymnoteapi.model.exercise.UpdateExerciseRequest;
import gymnote.gymnoteapi.security.service.UserDetailsImpl;
import gymnote.gymnoteapi.service.ExerciseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
        List<Exercise> exercises = exerciseService.getUserExercises(userDetails.getId());
        List<ExerciseDTO> exerciseDTOs = exercises.stream()
                .map(Exercise::toDTO)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(exerciseDTOs));
    }   

    @GetMapping("/{exerciseId}")
    public ResponseEntity<ApiResponse<ExerciseDTO>> getExerciseById(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @PathVariable Long exerciseId) {
        Exercise exercise = exerciseService.getUserExerciseById(exerciseId, userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success(exercise.toDTO()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ExerciseDTO>> createExercise(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody CreateExerciseRequest request) {
        Exercise exercise = request.toEntity();
        Exercise saved = exerciseService.createExercise(exercise, userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success(saved.toDTO()));
    }

    @PutMapping("/{exerciseId}")
    public ResponseEntity<ApiResponse<ExerciseDTO>> updateExercise(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @PathVariable Long exerciseId,
            @Valid @RequestBody UpdateExerciseRequest updateExerciseRequest) {
        Exercise exerciseData = updateExerciseRequest.toEntity();
        Exercise updated = exerciseService.updateExercise(exerciseId, userDetails.getId(), exerciseData);
        return ResponseEntity.ok(ApiResponse.success(updated.toDTO()));
    }

    @DeleteMapping("/{exerciseId}")
    public ResponseEntity<ApiResponse<Void>> deleteExercise(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @PathVariable Long exerciseId) {
        exerciseService.deleteExercise(exerciseId, userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}