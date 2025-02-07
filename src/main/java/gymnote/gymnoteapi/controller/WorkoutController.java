package gymnote.gymnoteapi.controller;

import gymnote.gymnoteapi.entity.Workout;
import gymnote.gymnoteapi.exception.workout.WorkoutCreationException;
import gymnote.gymnoteapi.exception.workout.WorkoutNotFoundException;
import gymnote.gymnoteapi.mapper.WorkoutMapper;
import gymnote.gymnoteapi.model.dto.WorkoutDTO;
import gymnote.gymnoteapi.model.workout.CreateWorkoutRequest;
import gymnote.gymnoteapi.model.workout.UpdateWorkoutRequest;
import gymnote.gymnoteapi.model.workout.WorkoutsResponse;
import gymnote.gymnoteapi.security.service.UserDetailsImpl;
import gymnote.gymnoteapi.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
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
    public ResponseEntity<WorkoutsResponse> getUserWorkouts(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            List<Workout> workouts = workoutService.getUserWorkouts(userDetails.getId());
            WorkoutsResponse response = new WorkoutsResponse();
            response.setWorkouts(workouts.stream().map(WorkoutMapper::toDTO).toList());
            response.setCount(workouts.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/workout/{id}")
    public ResponseEntity<WorkoutDTO> getUserWorkoutById(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id) {
        try {
            Workout workout = workoutService.getUserWorkoutById(id, userDetails.getId());
            return ResponseEntity.ok(WorkoutMapper.toDTO(workout));
        } catch (WorkoutNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/template/{templateId}/workout")
     public ResponseEntity<WorkoutsResponse> getTemplateWorkouts(
             @AuthenticationPrincipal UserDetailsImpl userDetails,
             @PathVariable Long templateId) {
         try {
             List<Workout> workouts = workoutService.getUserTemplateWorkouts(templateId, userDetails.getId());
             WorkoutsResponse response = new WorkoutsResponse();
             response.setWorkouts(workouts.stream().map(WorkoutMapper::toDTO).toList());
             response.setCount(workouts.size());
             return ResponseEntity.ok(response);
         } catch (Exception e) {
             return ResponseEntity.badRequest().build();
         }
     }


    @PostMapping
    public ResponseEntity<WorkoutDTO> createWorkout(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CreateWorkoutRequest createWorkoutRequest) {
        try {
            Workout workout = createWorkoutRequest.toEntity();
            Workout saved = workoutService.createWorkout(workout, createWorkoutRequest.getTemplateId(), userDetails.getId());
            return ResponseEntity.ok(WorkoutMapper.toDTO(saved));
        } catch (WorkoutCreationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/workout/start")
    public ResponseEntity<WorkoutDTO> createWorkoutFromTemplate(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Param("templateId") Long templateId
    ) {
        try {
            if (templateId == null) {
                return ResponseEntity.badRequest().build();
            }
            Workout saved = workoutService.createWorkoutFromTemplate(templateId, userDetails.getId());
            return ResponseEntity.ok(WorkoutMapper.toDTO(saved));
        } catch (WorkoutCreationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("workout/end/{workoutId}")
    public ResponseEntity<WorkoutDTO> endWorkout(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long workoutId
    ) {
        try {
            Workout workout = workoutService.endWorkout(workoutId, userDetails.getId());
            return ResponseEntity.ok(WorkoutMapper.toDTO(workout));
        } catch (WorkoutNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/workout/{id}")
    public ResponseEntity<WorkoutDTO> updateWorkout(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id,
            @RequestBody UpdateWorkoutRequest updateWorkoutRequest) {
        try {
            Workout workoutData = updateWorkoutRequest.toEntity();
            Workout updated = workoutService.updateUserWorkout(id, userDetails.getId(), workoutData);
            return ResponseEntity.ok(WorkoutMapper.toDTO(updated));
        } catch (WorkoutNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (WorkoutCreationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/workout/{id}")
    public ResponseEntity<?> deleteWorkout(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id) {
        try {
            workoutService.deleteWorkout(id, userDetails.getId());
            return ResponseEntity.noContent().build();
        } catch (WorkoutNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}