package gymnote.gymnoteapi.controller;

import gymnote.gymnoteapi.entity.WorkoutExercise;
import gymnote.gymnoteapi.mapper.WorkoutExerciseMapper;
import gymnote.gymnoteapi.model.api.ApiResponse;
import gymnote.gymnoteapi.model.dto.WorkoutExerciseDTO;
import gymnote.gymnoteapi.security.service.UserDetailsImpl;
import gymnote.gymnoteapi.service.WorkoutExerciseService;
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
@RequestMapping("/api/workout/{workoutId}/exercise")
public class WorkoutExerciseController {
    private final WorkoutExerciseService workoutExerciseService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<WorkoutExerciseDTO>>> getWorkoutExercises(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @PathVariable Long workoutId) {
        List<WorkoutExercise> exercises = workoutExerciseService.getWorkoutExercises(workoutId, userDetails.getId());
        List<WorkoutExerciseDTO> exerciseDTOs = exercises.stream()
            .map(WorkoutExerciseMapper::toDTO)
            .toList();
        return ResponseEntity.ok(ApiResponse.success(exerciseDTOs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkoutExerciseDTO>> getWorkoutExerciseById(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @PathVariable Long workoutId,
            @Valid @PathVariable Long id) {
        WorkoutExercise exercise = workoutExerciseService.getWorkoutExerciseById(id, workoutId, userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success(WorkoutExerciseMapper.toDTO(exercise)));
    }
}