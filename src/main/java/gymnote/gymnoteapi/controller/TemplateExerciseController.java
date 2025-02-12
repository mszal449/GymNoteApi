package gymnote.gymnoteapi.controller;

import gymnote.gymnoteapi.entity.TemplateExercise;
import gymnote.gymnoteapi.exception.template.TemplateNotFoundException;
import gymnote.gymnoteapi.exception.templateExercise.TemplateExerciseDuplicateOrderException;
import gymnote.gymnoteapi.exception.templateExercise.TemplateExerciseNotFoundException;
import gymnote.gymnoteapi.model.api.ApiResponse;
import gymnote.gymnoteapi.model.dto.TemplateExerciseDTO;
import gymnote.gymnoteapi.model.templateExercise.CreateTemplateExerciseRequest;
import gymnote.gymnoteapi.security.service.UserDetailsImpl;
import gymnote.gymnoteapi.service.TemplateExerciseService;
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
@RequestMapping("/api/template/{templateId}/exercise")
public class TemplateExerciseController {
    private final TemplateExerciseService templateExerciseService;

    // Template Exercise endpoints
    @GetMapping
    public ResponseEntity<ApiResponse<List<TemplateExerciseDTO>>> getTemplateExercises(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long templateId) {
        try {
            List<TemplateExercise> exercises = templateExerciseService.getUserTemplateExercises(templateId, userDetails.getId());
            List<TemplateExerciseDTO> exerciseDTOs = exercises.stream()
                .map(TemplateExercise::toDTO)
                .toList();
            return ResponseEntity.ok(ApiResponse.success(exerciseDTOs));
        } catch (TemplateNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Template not found"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to fetch template exercises"));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TemplateExerciseDTO>> addTemplateExercise(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long templateId,
            @RequestBody CreateTemplateExerciseRequest createTemplateExerciseRequest) {
        try {
            TemplateExercise exerciseData = createTemplateExerciseRequest.toEntity();
            TemplateExercise created = templateExerciseService.addUserTemplateExercise(
                    templateId, userDetails.getId(), exerciseData
            );
            return ResponseEntity.ok(ApiResponse.success(created.toDTO()));
        } catch (TemplateNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Template not found"));
        } catch (TemplateExerciseDuplicateOrderException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to create template exercise"));
        }
    }

    @PutMapping("/{exerciseId}")
    public ResponseEntity<ApiResponse<TemplateExerciseDTO>> updateTemplateExercise(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long templateId,
            @PathVariable Long exerciseId,
            @RequestBody CreateTemplateExerciseRequest createTemplateExerciseRequest) {
        try {
            TemplateExercise exerciseData = createTemplateExerciseRequest.toEntity();
            TemplateExercise updated = templateExerciseService.updateUserTemplateExercise(
                    exerciseId, templateId, userDetails.getId(), exerciseData
            );
            return ResponseEntity.ok(ApiResponse.success(updated.toDTO()));
        } catch (TemplateNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Template not found"));
        } catch (TemplateExerciseDuplicateOrderException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to update template exercise"));
        }
    }

    @DeleteMapping("/{exerciseId}")
    public ResponseEntity<ApiResponse<Void>> deleteTemplateExercise(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long templateId,
            @PathVariable Long exerciseId) {
        try {
            templateExerciseService.deleteUserTemplateExercise(exerciseId, templateId, userDetails.getId());
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (TemplateNotFoundException | TemplateExerciseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Template or exercise not found"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to delete template exercise"));
        }
    }
}
