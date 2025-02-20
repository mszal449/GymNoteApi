package gymnote.gymnoteapi.controller;

import gymnote.gymnoteapi.entity.Exercise;
import gymnote.gymnoteapi.mapper.ExerciseMapper;
import gymnote.gymnoteapi.model.api.ApiResponse;
import gymnote.gymnoteapi.model.dto.ExerciseDTO;
import gymnote.gymnoteapi.model.exercise.CreateExerciseRequest;
import gymnote.gymnoteapi.model.exercise.UpdateExerciseRequest;
import gymnote.gymnoteapi.security.service.CustomOAuth2User;
import gymnote.gymnoteapi.service.ExerciseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
            @AuthenticationPrincipal CustomOAuth2User user) {
        List<Exercise> exercises = exerciseService.getUserExercises(user.getId());
        List<ExerciseDTO> exerciseDTOs = exercises.stream()
                .map(Exercise::toDTO)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(exerciseDTOs));
    }   

    @GetMapping("/{exerciseId}")
    public ResponseEntity<ApiResponse<ExerciseDTO>> getExerciseById(
            @AuthenticationPrincipal CustomOAuth2User user,
            @Valid @PathVariable Long exerciseId) {
        Exercise exercise = exerciseService.getUserExerciseById(exerciseId, user.getId());
        return ResponseEntity.ok(ApiResponse.success(exercise.toDTO()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ExerciseDTO>> createExercise(
            @AuthenticationPrincipal CustomOAuth2User user,
            @Valid @RequestBody CreateExerciseRequest request) {
        Exercise exercise = ExerciseMapper.toEntity(request);
        Exercise saved = exerciseService.createExercise(exercise, user.getId());
        return ResponseEntity.ok(ApiResponse.success(saved.toDTO()));
    }

    @PutMapping("/{exerciseId}")
    public ResponseEntity<ApiResponse<ExerciseDTO>> updateExercise(
            @AuthenticationPrincipal CustomOAuth2User user,
            @Valid @PathVariable Long exerciseId,
            @Valid @RequestBody UpdateExerciseRequest updateExerciseRequest) {
        Exercise exerciseData = ExerciseMapper.toEntity(updateExerciseRequest);
        Exercise updated = exerciseService.updateExercise(exerciseId, user.getId(), exerciseData);
        return ResponseEntity.ok(ApiResponse.success(updated.toDTO()));
    }

    @DeleteMapping("/{exerciseId}")
    public ResponseEntity<ApiResponse<Void>> deleteExercise(
            @AuthenticationPrincipal CustomOAuth2User user,
            @Valid @PathVariable Long exerciseId) {
        exerciseService.deleteExercise(exerciseId, user.getId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}