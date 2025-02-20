package gymnote.gymnoteapi.controller;

import gymnote.gymnoteapi.entity.Workout;
import gymnote.gymnoteapi.mapper.WorkoutMapper;
import gymnote.gymnoteapi.model.api.ApiResponse;
import gymnote.gymnoteapi.model.dto.WorkoutDTO;
import gymnote.gymnoteapi.model.workout.UpdateWorkoutRequest;
import gymnote.gymnoteapi.security.service.CustomOAuth2User;
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
            @AuthenticationPrincipal CustomOAuth2User user) {
        List<Workout> workouts = workoutService.getUserWorkouts(user.getId());
        List<WorkoutDTO> workoutDTOs = workouts.stream()
                .map(WorkoutMapper::toDTO)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(workoutDTOs));
    }

    @GetMapping("/workout/{id}")
    public ResponseEntity<ApiResponse<WorkoutDTO>> getUserWorkoutById(
            @AuthenticationPrincipal CustomOAuth2User user,
            @Valid @PathVariable Long id) {
        Workout workout = workoutService.getUserWorkoutById(id, user.getId());
        return ResponseEntity.ok(ApiResponse.success(WorkoutMapper.toDTO(workout)));
    }

    @GetMapping("/template/{templateId}/workout")
    public ResponseEntity<ApiResponse<List<WorkoutDTO>>> getTemplateWorkouts(
            @AuthenticationPrincipal CustomOAuth2User user,
            @Valid @PathVariable Long templateId) {
        List<Workout> workouts = workoutService.getUserTemplateWorkouts(templateId, user.getId());
        List<WorkoutDTO> workoutDTOs = workouts.stream()
                .map(WorkoutMapper::toDTO)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(workoutDTOs));
    }

    @PostMapping("/template/{templateId}/start")
    public ResponseEntity<ApiResponse<WorkoutDTO>> createWorkoutFromTemplate(
            @AuthenticationPrincipal CustomOAuth2User user,
            @Valid @PathVariable Long templateId) {
        Workout saved = workoutService.createWorkoutFromTemplate(templateId, user.getId());
        return ResponseEntity.ok(ApiResponse.success(WorkoutMapper.toDTO(saved)));
    }

    @PostMapping("/workout/end/{workoutId}")
    public ResponseEntity<ApiResponse<WorkoutDTO>> endWorkout(
            @AuthenticationPrincipal CustomOAuth2User user,
            @Valid @PathVariable Long workoutId) {
        Workout workout = workoutService.endWorkout(workoutId, user.getId());
        return ResponseEntity.ok(ApiResponse.success(WorkoutMapper.toDTO(workout)));
    }

    @PutMapping("/workout/{id}")
    public ResponseEntity<ApiResponse<WorkoutDTO>> updateWorkout(
            @AuthenticationPrincipal CustomOAuth2User user,
            @Valid @PathVariable Long id,
            @Valid @RequestBody UpdateWorkoutRequest updateWorkoutRequest) {
        Workout workoutData = WorkoutMapper.toEntity(updateWorkoutRequest);
        Workout updated = workoutService.updateUserWorkout(id, user.getId(), workoutData);
        return ResponseEntity.ok(ApiResponse.success(WorkoutMapper.toDTO(updated)));
    }

    @DeleteMapping("/workout/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteWorkout(
            @AuthenticationPrincipal CustomOAuth2User user,
            @PathVariable Long id) {
        workoutService.deleteWorkout(id, user.getId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}