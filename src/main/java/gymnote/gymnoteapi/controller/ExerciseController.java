package gymnote.gymnoteapi.controller;

import gymnote.gymnoteapi.entity.Exercise;
import gymnote.gymnoteapi.model.dto.ExerciseDTO;
import gymnote.gymnoteapi.model.exercise.ExerciseResponse;
import gymnote.gymnoteapi.model.exercise.NewExerciseRequest;
import gymnote.gymnoteapi.security.service.UserDetailsImpl;
import gymnote.gymnoteapi.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
@RequestMapping("/api/exercise")
public class ExerciseController {
    private final ExerciseService exerciseService;


    @GetMapping("/{exerciseId}")
    public ResponseEntity<ExerciseResponse> getExerciseById(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long exerciseId) {

        Long userId = userDetails.getId();
        Optional<ExerciseDTO> exercise = exerciseService.getUserExerciseById(exerciseId, userId);

        if (exercise.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            ExerciseResponse exerciseResponse = new ExerciseResponse();
            exerciseResponse.setExercise(exercise.get());
            return ResponseEntity.ok(exerciseResponse);
        }
    }

    @PostMapping
    public ResponseEntity<ExerciseResponse> createExercise(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody NewExerciseRequest newExerciseRequest) {
        Long userId = userDetails.getId();

        Exercise exercise = new Exercise(newExerciseRequest);


        Optional<ExerciseDTO> newExercise = exerciseService.createExercise(exercise, userId);
        if (newExercise.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        ExerciseResponse exerciseResponse = new ExerciseResponse();
        exerciseResponse.setExercise(newExercise.get());
        return ResponseEntity.ok(exerciseResponse);
    }
}
