package gymnote.gymnoteapi.controller;

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
        Optional<ExerciseDTO> exerciseDTO = exerciseService.getUserExerciseById(exerciseId, userId);

        if (exerciseDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            ExerciseResponse exerciseResponse = new ExerciseResponse(exerciseDTO.get());
            return ResponseEntity.ok(exerciseResponse);
        }
    }

    @PostMapping
    public ResponseEntity<ExerciseResponse> createExercise(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody NewExerciseRequest newExerciseRequest) {
        Long userId = userDetails.getId();

        ExerciseDTO exerciseDTO = new ExerciseDTO(newExerciseRequest);
        Optional<ExerciseDTO> newExerciseDTO = exerciseService.createExercise(exerciseDTO, userId);

        if (newExerciseDTO.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        ExerciseResponse exerciseResponse = new ExerciseResponse(newExerciseDTO.get());
        return ResponseEntity.ok(exerciseResponse);
    }

    @PutMapping("/{exerciseId}")
    public ResponseEntity<ExerciseResponse> updateExercise(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long exerciseId,
            @RequestBody NewExerciseRequest updateExerciseRequest) {
        Long userId = userDetails.getId();

        ExerciseDTO exerciseDTO = new ExerciseDTO(updateExerciseRequest);
        Optional<ExerciseDTO> updatedExerciseDTO = exerciseService.updateExercise(exerciseId, userId, exerciseDTO);

        if (updatedExerciseDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ExerciseResponse exerciseResponse = new ExerciseResponse(updatedExerciseDTO.get());
        return ResponseEntity.ok(exerciseResponse);
    }

    @DeleteMapping("/{exerciseId}")
    public ResponseEntity<Void> deleteExercise(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long exerciseId) {
        Long userId = userDetails.getId();

        boolean isDeleted = exerciseService.deleteExercise(exerciseId, userId);
        if (!isDeleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}