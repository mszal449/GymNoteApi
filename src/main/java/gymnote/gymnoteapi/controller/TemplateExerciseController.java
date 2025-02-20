package gymnote.gymnoteapi.controller;

import gymnote.gymnoteapi.entity.TemplateExercise;
import gymnote.gymnoteapi.mapper.TemplateExerciseMapper;
import gymnote.gymnoteapi.model.api.ApiResponse;
import gymnote.gymnoteapi.model.dto.TemplateExerciseDTO;
import gymnote.gymnoteapi.model.templateExercise.CreateTemplateExerciseRequest;
import gymnote.gymnoteapi.model.templateExercise.UpdateTemplateExerciseRequest;
import gymnote.gymnoteapi.security.service.CustomOAuth2User;
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
            @AuthenticationPrincipal CustomOAuth2User user,
            @Valid @PathVariable Long templateId,
            @RequestParam(defaultValue = "false") boolean includeExercises) {
        List<TemplateExercise> exercises = templateExerciseService.getUserTemplateExercises(templateId, user.getId(), includeExercises);
        List<TemplateExerciseDTO> exerciseDTOs = exercises.stream()
                .map(exercise -> exercise.toDTO(includeExercises))
                .toList();
        return ResponseEntity.ok(ApiResponse.success(exerciseDTOs));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TemplateExerciseDTO>> addTemplateExercise(
            @AuthenticationPrincipal CustomOAuth2User user,
            @Valid @PathVariable Long templateId,
            @Valid @RequestBody CreateTemplateExerciseRequest createTemplateExerciseRequest) {
            TemplateExercise exerciseData = TemplateExerciseMapper.toEntity(createTemplateExerciseRequest);
            TemplateExercise created = templateExerciseService.addUserTemplateExercise(
                    templateId, user.getId(), exerciseData
            );
            return ResponseEntity.ok(ApiResponse.success(created.toDTO()));
    }

    @PutMapping("/{exerciseId}")
    public ResponseEntity<ApiResponse<TemplateExerciseDTO>> updateTemplateExercise(
            @AuthenticationPrincipal CustomOAuth2User user,
            @Valid @PathVariable Long templateId,
            @Valid @PathVariable Long exerciseId,
            @Valid @RequestBody UpdateTemplateExerciseRequest updateTemplateExerciseRequest) {
        TemplateExercise exerciseData = TemplateExerciseMapper.toEntity(updateTemplateExerciseRequest);
        TemplateExercise updated = templateExerciseService.updateUserTemplateExercise(
                exerciseId, templateId, user.getId(), exerciseData
        );

        return ResponseEntity.ok(ApiResponse.success(updated.toDTO()));
    }

    @DeleteMapping("/{exerciseId}")
    public ResponseEntity<ApiResponse<Void>> deleteTemplateExercise(
            @AuthenticationPrincipal CustomOAuth2User user,
            @Valid @PathVariable Long templateId,
            @Valid @PathVariable Long exerciseId) {
        templateExerciseService.deleteUserTemplateExercise(exerciseId, templateId, user.getId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
