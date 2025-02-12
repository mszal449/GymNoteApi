package gymnote.gymnoteapi.controller;

import gymnote.gymnoteapi.entity.Workout;
import gymnote.gymnoteapi.mapper.WorkoutMapper;
import gymnote.gymnoteapi.model.api.ApiResponse;
import gymnote.gymnoteapi.model.dto.WorkoutDTO;
import gymnote.gymnoteapi.model.workout.UpdateWorkoutRequest;
import gymnote.gymnoteapi.security.service.UserDetailsImpl;
import gymnote.gymnoteapi.service.WorkoutService;
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
@RequestMapping("/api")
public class WorkoutController {
    private final WorkoutService workoutService;

    @GetMapping("/workout")
    public ResponseEntity<ApiResponse<List<WorkoutDTO>>> getUserWorkouts(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<Workout> workouts = workoutService.getUserWorkouts(userDetails.getId());
        List<WorkoutDTO> workoutDTOs = workouts.stream()
                .map(WorkoutMapper::toDTO)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(workoutDTOs));
    }

    @GetMapping("/workout/{id}")
    public ResponseEntity<ApiResponse<WorkoutDTO>> getUserWorkoutById(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @PathVariable Long id) {
        Workout workout = workoutService.getUserWorkoutById(id, userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success(WorkoutMapper.toDTO(workout)));
    }

    @GetMapping("/template/{templateId}/workout")
    public ResponseEntity<ApiResponse<List<WorkoutDTO>>> getTemplateWorkouts(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @PathVariable Long templateId) {
        List<Workout> workouts = workoutService.getUserTemplateWorkouts(templateId, userDetails.getId());
        List<WorkoutDTO> workoutDTOs = workouts.stream()
                .map(WorkoutMapper::toDTO)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(workoutDTOs));
    }

    @PostMapping("/template/{templateId}/start")
    public ResponseEntity<ApiResponse<WorkoutDTO>> createWorkoutFromTemplate(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @PathVariable Long templateId) {
        Workout saved = workoutService.createWorkoutFromTemplate(templateId, userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success(WorkoutMapper.toDTO(saved)));
    }

    @PostMapping("/workout/end/{workoutId}")
    public ResponseEntity<ApiResponse<WorkoutDTO>> endWorkout(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @PathVariable Long workoutId) {
        Workout workout = workoutService.endWorkout(workoutId, userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success(WorkoutMapper.toDTO(workout)));
    }

    @PutMapping("/workout/{id}")
    public ResponseEntity<ApiResponse<WorkoutDTO>> updateWorkout(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @PathVariable Long id,
            @Valid @RequestBody UpdateWorkoutRequest updateWorkoutRequest) {
        Workout workoutData = updateWorkoutRequest.toEntity();
        Workout updated = workoutService.updateUserWorkout(id, userDetails.getId(), workoutData);
        return ResponseEntity.ok(ApiResponse.success(WorkoutMapper.toDTO(updated)));
    }

    @DeleteMapping("/workout/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteWorkout(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id) {
        workoutService.deleteWorkout(id, userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}