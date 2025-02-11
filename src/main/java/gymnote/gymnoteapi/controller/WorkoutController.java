package gymnote.gymnoteapi.controller;

import gymnote.gymnoteapi.entity.Workout;
import gymnote.gymnoteapi.exception.template.TemplateNotFoundException;
import gymnote.gymnoteapi.exception.workout.WorkoutCreationException;
import gymnote.gymnoteapi.exception.workout.WorkoutNotFoundException;
import gymnote.gymnoteapi.mapper.WorkoutMapper;
import gymnote.gymnoteapi.model.api.ApiResponse;
import gymnote.gymnoteapi.model.dto.WorkoutDTO;
import gymnote.gymnoteapi.model.workout.UpdateWorkoutRequest;
import gymnote.gymnoteapi.security.service.UserDetailsImpl;
import gymnote.gymnoteapi.service.WorkoutService;
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
@RequestMapping("/api")
public class WorkoutController {
    private final WorkoutService workoutService;

    @GetMapping("/workout")
    public ResponseEntity<ApiResponse<List<WorkoutDTO>>> getUserWorkouts(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            List<Workout> workouts = workoutService.getUserWorkouts(userDetails.getId());
            List<WorkoutDTO> workoutDTOs = workouts.stream()
                    .map(WorkoutMapper::toDTO)
                    .toList();
            return ResponseEntity.ok(ApiResponse.success(workoutDTOs));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to fetch workouts"));
        }
    }

    @GetMapping("/workout/{id}")
    public ResponseEntity<ApiResponse<WorkoutDTO>> getUserWorkoutById(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id) {
        try {
            Workout workout = workoutService.getUserWorkoutById(id, userDetails.getId());
            return ResponseEntity.ok(ApiResponse.success(WorkoutMapper.toDTO(workout)));
        } catch (WorkoutNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Workout not found"));
        }
    }

    @GetMapping("/template/{templateId}/workout")
    public ResponseEntity<ApiResponse<List<WorkoutDTO>>> getTemplateWorkouts(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long templateId) {
        try {
            List<Workout> workouts = workoutService.getUserTemplateWorkouts(templateId, userDetails.getId());
            List<WorkoutDTO> workoutDTOs = workouts.stream()
                    .map(WorkoutMapper::toDTO)
                    .toList();
            return ResponseEntity.ok(ApiResponse.success(workoutDTOs));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to fetch template workouts"));
        }
    }

    @PostMapping("/templates/{templateId}/start")
    public ResponseEntity<ApiResponse<WorkoutDTO>> createWorkoutFromTemplate(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long templateId) {
        try {
            Workout saved = workoutService.createWorkoutFromTemplate(templateId, userDetails.getId());
            return ResponseEntity.ok(ApiResponse.success(WorkoutMapper.toDTO(saved)));
        } catch (TemplateNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Template not found: " + templateId));
        } catch (WorkoutCreationException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to create workout from template"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Unexpected error while creating workout"));
        }
    }

    @PostMapping("/workout/end/{workoutId}")
    public ResponseEntity<ApiResponse<WorkoutDTO>> endWorkout(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long workoutId) {
        try {
            Workout workout = workoutService.endWorkout(workoutId, userDetails.getId());
            return ResponseEntity.ok(ApiResponse.success(WorkoutMapper.toDTO(workout)));
        } catch (WorkoutNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Workout not found"));
        }
    }

    @PutMapping("/workout/{id}")
    public ResponseEntity<ApiResponse<WorkoutDTO>> updateWorkout(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id,
            @RequestBody UpdateWorkoutRequest updateWorkoutRequest) {
        try {
            Workout workoutData = updateWorkoutRequest.toEntity();
            Workout updated = workoutService.updateUserWorkout(id, userDetails.getId(), workoutData);
            return ResponseEntity.ok(ApiResponse.success(WorkoutMapper.toDTO(updated)));
        } catch (WorkoutNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Workout not found"));
        } catch (WorkoutCreationException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to update workout"));
        }
    }

    @DeleteMapping("/workout/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteWorkout(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id) {
        try {
            workoutService.deleteWorkout(id, userDetails.getId());
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (WorkoutNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Workout not found"));
        }
    }
}