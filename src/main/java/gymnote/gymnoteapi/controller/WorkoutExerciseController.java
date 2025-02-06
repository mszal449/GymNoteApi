package gymnote.gymnoteapi.controller;

import gymnote.gymnoteapi.entity.WorkoutExercise;
import gymnote.gymnoteapi.mapper.WorkoutExerciseMapper;
import gymnote.gymnoteapi.model.dto.WorkoutExerciseDTO;
import gymnote.gymnoteapi.model.workoutExercise.CreateWorkoutExerciseRequest;
import gymnote.gymnoteapi.model.workoutExercise.UpdateWorkoutExerciseRequest;
import gymnote.gymnoteapi.model.workoutExercise.WorkoutExercisesResponse;
import gymnote.gymnoteapi.security.service.UserDetailsImpl;
import gymnote.gymnoteapi.service.WorkoutExerciseService;
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
    public ResponseEntity<WorkoutExercisesResponse> getWorkoutExercises(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long workoutId) {
        try {
            List<WorkoutExercise> exercises = workoutExerciseService.getWorkoutExercises(workoutId, userDetails.getId());
            WorkoutExercisesResponse response = new WorkoutExercisesResponse();
            response.setExercises(exercises.stream().map(WorkoutExerciseMapper::toDTO).toList());
            response.setCount(exercises.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutExerciseDTO> getWorkoutExerciseById(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long workoutId,
            @PathVariable Long id) {
        try {
            WorkoutExercise exercise = workoutExerciseService.getWorkoutExerciseById(id, workoutId, userDetails.getId());
            return ResponseEntity.ok(WorkoutExerciseMapper.toDTO(exercise));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<WorkoutExerciseDTO> createWorkoutExercise(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long workoutId,
            @RequestBody CreateWorkoutExerciseRequest createRequest) {
        try {
            WorkoutExercise exercise = createRequest.toEntity();
            WorkoutExercise saved = workoutExerciseService.createWorkoutExercise(exercise, workoutId, userDetails.getId());
            return ResponseEntity.ok(WorkoutExerciseMapper.toDTO(saved));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutExerciseDTO> updateWorkoutExercise(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long workoutId,
            @PathVariable Long id,
            @RequestBody UpdateWorkoutExerciseRequest updateRequest) {
        try {
            WorkoutExercise exerciseData = updateRequest.toEntity();
            WorkoutExercise updated = workoutExerciseService.updateWorkoutExercise(
                    id, workoutId, userDetails.getId(), exerciseData);
            return ResponseEntity.ok(WorkoutExerciseMapper.toDTO(updated));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWorkoutExercise(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long workoutId,
            @PathVariable Long id) {
        try {
            workoutExerciseService.deleteWorkoutExercise(id, workoutId, userDetails.getId());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}