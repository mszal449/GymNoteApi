package gymnote.gymnoteapi.controller;

import gymnote.gymnoteapi.entity.Exercise;
import gymnote.gymnoteapi.exception.UserNotFoundException;
import gymnote.gymnoteapi.exception.exercise.ExerciseCreationException;
import gymnote.gymnoteapi.exception.exercise.ExerciseNotFoundException;
import gymnote.gymnoteapi.model.dto.ExerciseDTO;
import gymnote.gymnoteapi.model.exercise.CreateExerciseRequest;
import gymnote.gymnoteapi.model.exercise.ExercisesResponse;
import gymnote.gymnoteapi.model.exercise.UpdateExerciseRequest;
import gymnote.gymnoteapi.security.service.UserDetailsImpl;
import gymnote.gymnoteapi.service.ExerciseService;
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
@RequestMapping("/api/exercise")
public class ExerciseController {
    private final ExerciseService exerciseService;

    @GetMapping()
    public ResponseEntity<ExercisesResponse> getExercises(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<Exercise> exercises = exerciseService.getUserExercises(userDetails.getId());

        ExercisesResponse response = new ExercisesResponse();
        response.setExercises(exercises.stream().map(Exercise::toDTO).toList());
        response.setCount(exercises.size());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{exerciseId}")
    public ResponseEntity<ExerciseDTO> getExerciseById(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long exerciseId) {
        try {
            Exercise exercise = exerciseService.getUserExerciseById(exerciseId, userDetails.getId());
            return ResponseEntity.ok(exercise.toDTO());
        } catch (ExerciseNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping
    public ResponseEntity<ExerciseDTO> createExercise(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CreateExerciseRequest request) {
        try {
            Exercise exercise = request.toEntity();
            Exercise saved = exerciseService.createExercise(exercise, userDetails.getId());

            return ResponseEntity.ok(saved.toDTO());
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ExerciseCreationException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @PutMapping("/{exerciseId}")
    public ResponseEntity<ExerciseDTO> updateExercise(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long exerciseId,
            @RequestBody UpdateExerciseRequest updateExerciseRequest) {
        try{
            Exercise exerciseData = updateExerciseRequest.toEntity();
            Exercise updated = exerciseService.updateExercise(exerciseId, userDetails.getId(), exerciseData);

            return ResponseEntity.ok(updated.toDTO());
        } catch (ExerciseNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ExerciseCreationException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @DeleteMapping("/{exerciseId}")
    public ResponseEntity<Void> deleteExercise(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long exerciseId) {
        try {
            exerciseService.deleteExercise(exerciseId, userDetails.getId());
            return ResponseEntity.noContent().build();
        } catch (ExerciseNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}