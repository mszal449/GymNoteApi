package gymnote.gymnoteapi.controller;

import gymnote.gymnoteapi.entity.ExerciseSet;
import gymnote.gymnoteapi.exception.exerciseSet.ExerciseSetNotFoundException;
import gymnote.gymnoteapi.mapper.ExerciseSetMapper;
import gymnote.gymnoteapi.model.api.ApiResponse;
import gymnote.gymnoteapi.model.dto.ExerciseSetDTO;
import gymnote.gymnoteapi.model.exerciseSet.CreateExerciseSetRequest;
import gymnote.gymnoteapi.model.exerciseSet.UpdateExerciseSetRequest;
import gymnote.gymnoteapi.security.service.UserDetailsImpl;
import gymnote.gymnoteapi.service.ExerciseSetService;
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
@RequestMapping("/api/workout/{workoutId}/exercise/{exerciseId}/set")
public class ExerciseSetController {
    private final ExerciseSetService exerciseSetService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ExerciseSetDTO>>> getExerciseSets(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long workoutId,
            @PathVariable Long exerciseId) {
        try {
            List<ExerciseSet> sets = exerciseSetService.getExerciseSets(workoutId, exerciseId, userDetails.getId());
            List<ExerciseSetDTO> setDTOs = sets.stream()
                .map(ExerciseSetMapper::toDTO)
                .toList();
            return ResponseEntity.ok(ApiResponse.success(setDTOs));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to fetch exercise sets"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExerciseSetDTO>> getExerciseSetById(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long workoutId,
            @PathVariable Long exerciseId,
            @PathVariable Long id) {
        try {
            ExerciseSet set = exerciseSetService.getExerciseSetById(id, exerciseId, workoutId, userDetails.getId());
            return ResponseEntity.ok(ApiResponse.success(ExerciseSetMapper.toDTO(set)));
        } catch (ExerciseSetNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Exercise set not found"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to fetch exercise set"));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ExerciseSetDTO>> createExerciseSet(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long workoutId,
            @PathVariable Long exerciseId,
            @RequestBody CreateExerciseSetRequest createRequest) {
        try {
            ExerciseSet set = ExerciseSetMapper.toEntity(createRequest);
            ExerciseSet saved = exerciseSetService.createExerciseSet(set, exerciseId, workoutId, userDetails.getId());
            return ResponseEntity.ok(ApiResponse.success(ExerciseSetMapper.toDTO(saved)));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to create exercise set: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ExerciseSetDTO>> updateExerciseSet(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long workoutId,
            @PathVariable Long exerciseId,
            @PathVariable Long id,
            @RequestBody UpdateExerciseSetRequest updateRequest) {
        try {
            ExerciseSet setData = ExerciseSetMapper.toEntity(updateRequest);
            ExerciseSet updated = exerciseSetService.updateExerciseSet(id, setData, exerciseId, workoutId, userDetails.getId());
            return ResponseEntity.ok(ApiResponse.success(ExerciseSetMapper.toDTO(updated)));
        } catch (ExerciseSetNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Exercise set not found"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to update exercise set"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteExerciseSet(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long workoutId,
            @PathVariable Long exerciseId,
            @PathVariable Long id) {
        try {
            exerciseSetService.deleteExerciseSet(id, exerciseId, workoutId, userDetails.getId());
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (ExerciseSetNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Exercise set not found"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to delete exercise set"));
        }
    }
}