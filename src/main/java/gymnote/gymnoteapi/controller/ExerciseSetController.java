package gymnote.gymnoteapi.controller;

import gymnote.gymnoteapi.entity.ExerciseSet;
import gymnote.gymnoteapi.exception.exerciseSet.ExerciseSetNotFoundException;
import gymnote.gymnoteapi.mapper.ExerciseSetMapper;
import gymnote.gymnoteapi.model.dto.ExerciseSetDTO;
import gymnote.gymnoteapi.model.exerciseSet.CreateExerciseSetRequest;
import gymnote.gymnoteapi.model.exerciseSet.ExerciseSetsResponse;
import gymnote.gymnoteapi.model.exerciseSet.UpdateExerciseSetRequest;
import gymnote.gymnoteapi.security.service.UserDetailsImpl;
import gymnote.gymnoteapi.service.ExerciseSetService;
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
    public ResponseEntity<ExerciseSetsResponse> getExerciseSets(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long workoutId,
            @PathVariable Long exerciseId) {
        try {
            List<ExerciseSet> sets = exerciseSetService.getExerciseSets(workoutId, exerciseId, userDetails.getId());
            ExerciseSetsResponse response = new ExerciseSetsResponse();
            response.setSets(sets.stream().map(ExerciseSetMapper::toDTO).toList());
            response.setCount(sets.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseSetDTO> getExerciseSetById(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long workoutId,
            @PathVariable Long exerciseId,
            @PathVariable Long id) {
        try {
            ExerciseSet set = exerciseSetService.getExerciseSetById(id, exerciseId, workoutId, userDetails.getId());
            return ResponseEntity.ok(ExerciseSetMapper.toDTO(set));
        } catch (ExerciseSetNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<ExerciseSetDTO> createExerciseSet(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long workoutId,
            @PathVariable Long exerciseId,
            @RequestBody CreateExerciseSetRequest createRequest) {
        try {
            ExerciseSet set = ExerciseSetMapper.toEntity(createRequest);
            ExerciseSet saved = exerciseSetService.createExerciseSet(set, exerciseId, workoutId, userDetails.getId());
            return ResponseEntity.ok(ExerciseSetMapper.toDTO(saved));
        } catch (Exception e) {
            System.out.println(e.getMessage());
//            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExerciseSetDTO> updateExerciseSet(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long workoutId,
            @PathVariable Long exerciseId,
            @PathVariable Long id,
            @RequestBody UpdateExerciseSetRequest updateRequest) {
        try {
            ExerciseSet setData = ExerciseSetMapper.toEntity(updateRequest);
            ExerciseSet updated = exerciseSetService.updateExerciseSet(id, setData, exerciseId, workoutId, userDetails.getId());
            return ResponseEntity.ok(ExerciseSetMapper.toDTO(updated));
        } catch (ExerciseSetNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExerciseSet(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long workoutId,
            @PathVariable Long exerciseId,
            @PathVariable Long id) {
        try {
            exerciseSetService.deleteExerciseSet(id, exerciseId, workoutId, userDetails.getId());
            return ResponseEntity.noContent().build();
        } catch (ExerciseSetNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}