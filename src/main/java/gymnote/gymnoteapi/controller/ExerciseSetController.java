package gymnote.gymnoteapi.controller;

import gymnote.gymnoteapi.entity.ExerciseSet;
import gymnote.gymnoteapi.mapper.ExerciseSetMapper;
import gymnote.gymnoteapi.model.api.ApiResponse;
import gymnote.gymnoteapi.model.dto.ExerciseSetDTO;
import gymnote.gymnoteapi.model.exerciseSet.CreateExerciseSetRequest;
import gymnote.gymnoteapi.model.exerciseSet.UpdateExerciseSetRequest;
import gymnote.gymnoteapi.security.service.UserDetailsImpl;
import gymnote.gymnoteapi.service.ExerciseSetService;
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
@RequestMapping("/api/workout/{workoutId}/exercise/{exerciseId}/set")
public class ExerciseSetController {
    private final ExerciseSetService exerciseSetService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ExerciseSetDTO>>> getExerciseSets(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @PathVariable Long workoutId,
            @Valid @PathVariable Long exerciseId) {
        List<ExerciseSet> sets = exerciseSetService.getExerciseSets(workoutId, exerciseId, userDetails.getId());
        List<ExerciseSetDTO> setDTOs = sets.stream()
            .map(ExerciseSetMapper::toDTO)
            .toList();
        return ResponseEntity.ok(ApiResponse.success(setDTOs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExerciseSetDTO>> getExerciseSetById(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @PathVariable Long workoutId,
            @Valid @PathVariable Long exerciseId,
            @Valid @PathVariable Long id) {
        ExerciseSet set = exerciseSetService.getExerciseSetById(id, exerciseId, workoutId, userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success(ExerciseSetMapper.toDTO(set)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ExerciseSetDTO>> createExerciseSet(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @PathVariable Long workoutId,
            @Valid @PathVariable Long exerciseId,
            @Valid @RequestBody CreateExerciseSetRequest createRequest) {
        ExerciseSet set = ExerciseSetMapper.toEntity(createRequest);
        ExerciseSet saved = exerciseSetService.createExerciseSet(set, exerciseId, workoutId, userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success(ExerciseSetMapper.toDTO(saved)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ExerciseSetDTO>> updateExerciseSet(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @PathVariable Long workoutId,
            @Valid @PathVariable Long exerciseId,
            @Valid @PathVariable Long id,
            @Valid @RequestBody UpdateExerciseSetRequest updateRequest) {
        ExerciseSet setData = ExerciseSetMapper.toEntity(updateRequest);
        ExerciseSet updated = exerciseSetService.updateExerciseSet(id, setData, exerciseId, workoutId, userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success(ExerciseSetMapper.toDTO(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteExerciseSet(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @PathVariable Long workoutId,
            @Valid @PathVariable Long exerciseId,
            @Valid @PathVariable Long id) {
        exerciseSetService.deleteExerciseSet(id, exerciseId, workoutId, userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}