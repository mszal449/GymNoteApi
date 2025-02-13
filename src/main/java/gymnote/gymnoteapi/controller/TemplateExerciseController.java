package gymnote.gymnoteapi.controller;

import gymnote.gymnoteapi.entity.TemplateExercise;
import gymnote.gymnoteapi.mapper.TemplateExerciseMapper;
import gymnote.gymnoteapi.model.api.ApiResponse;
import gymnote.gymnoteapi.model.dto.TemplateExerciseDTO;
import gymnote.gymnoteapi.model.templateExercise.CreateTemplateExerciseRequest;
import gymnote.gymnoteapi.model.templateExercise.UpdateTemplateExerciseRequest;
import gymnote.gymnoteapi.security.service.UserDetailsImpl;
import gymnote.gymnoteapi.service.TemplateExerciseService;
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
@RequestMapping("/api/template/{templateId}/exercise")
public class TemplateExerciseController {
    private final TemplateExerciseService templateExerciseService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TemplateExerciseDTO>>> getTemplateExercises(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @PathVariable Long templateId) {
        List<TemplateExercise> exercises = templateExerciseService.getUserTemplateExercises(templateId, userDetails.getId());
        List<TemplateExerciseDTO> exerciseDTOs = exercises.stream()
            .map(TemplateExercise::toDTO)
            .toList();
        return ResponseEntity.ok(ApiResponse.success(exerciseDTOs));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TemplateExerciseDTO>> addTemplateExercise(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @PathVariable Long templateId,
            @Valid @RequestBody CreateTemplateExerciseRequest createTemplateExerciseRequest) {
            TemplateExercise exerciseData = TemplateExerciseMapper.toEntity(createTemplateExerciseRequest);
            TemplateExercise created = templateExerciseService.addUserTemplateExercise(
                    templateId, userDetails.getId(), exerciseData
            );
            return ResponseEntity.ok(ApiResponse.success(created.toDTO()));
    }

    @PutMapping("/{exerciseId}")
    public ResponseEntity<ApiResponse<TemplateExerciseDTO>> updateTemplateExercise(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @PathVariable Long templateId,
            @Valid @PathVariable Long exerciseId,
            @Valid @RequestBody UpdateTemplateExerciseRequest updateTemplateExerciseRequest) {
        TemplateExercise exerciseData = TemplateExerciseMapper.toEntity(updateTemplateExerciseRequest);
        TemplateExercise updated = templateExerciseService.updateUserTemplateExercise(
                exerciseId, templateId, userDetails.getId(), exerciseData
        );

        return ResponseEntity.ok(ApiResponse.success(updated.toDTO()));
    }

    @DeleteMapping("/{exerciseId}")
    public ResponseEntity<ApiResponse<Void>> deleteTemplateExercise(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @PathVariable Long templateId,
            @Valid @PathVariable Long exerciseId) {
        templateExerciseService.deleteUserTemplateExercise(exerciseId, templateId, userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
