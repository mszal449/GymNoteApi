package gymnote.gymnoteapi.controller;

import gymnote.gymnoteapi.entity.Template;
import gymnote.gymnoteapi.entity.TemplateExercise;
import gymnote.gymnoteapi.exception.UserNotFoundException;
import gymnote.gymnoteapi.exception.template.TemplateCreationException;
import gymnote.gymnoteapi.exception.template.TemplateNotFoundException;
import gymnote.gymnoteapi.exception.template.TemplateUpdateException;
import gymnote.gymnoteapi.exception.templateExercise.TemplateExerciseCreationException;
import gymnote.gymnoteapi.exception.templateExercise.TemplateExerciseNotFoundException;
import gymnote.gymnoteapi.model.api.ApiResponse;
import gymnote.gymnoteapi.model.dto.TemplateDTO;
import gymnote.gymnoteapi.model.dto.TemplateExerciseDTO;
import gymnote.gymnoteapi.model.template.CreateTemplateRequest;
import gymnote.gymnoteapi.model.template.UpdateTemplateRequest;
import gymnote.gymnoteapi.model.templateExercise.CreateTemplateExerciseRequest;
import gymnote.gymnoteapi.security.service.UserDetailsImpl;
import gymnote.gymnoteapi.service.TemplateExerciseService;
import gymnote.gymnoteapi.service.TemplateService;
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
@RequestMapping("/api/template")
public class TemplateController {
    public final TemplateService templateService;
    public final TemplateExerciseService templateExerciseService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TemplateDTO>>> getUserTemplates(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            List<Template> templates = templateService.getTemplatesByUserId(userDetails.getId());
            List<TemplateDTO> templateDTOs = templates.stream()
                .map(Template::toDTO)
                .toList();
            return ResponseEntity.ok(ApiResponse.success(templateDTOs));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to fetch templates"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TemplateDTO>> getUserTemplateById(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id) {
        try {
            Template template = templateService.getUserTemplateById(id, userDetails.getId());
            return ResponseEntity.ok(ApiResponse.success(template.toDTO()));
        } catch (TemplateNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Template not found"));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TemplateDTO>> createTemplate(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CreateTemplateRequest createTemplateRequest) {
        try {
            Template template = createTemplateRequest.toEntity();
            Template created = templateService.createUserTemplate(template, userDetails.getId());
            return ResponseEntity.ok(ApiResponse.success(created.toDTO()));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("User not found"));
        } catch (TemplateCreationException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to create template"));
        }
    }

    @PutMapping("/{templateId}")
    public ResponseEntity<ApiResponse<TemplateDTO>> updateTemplate(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long templateId,
            @RequestBody UpdateTemplateRequest newTemplateRequest) {
        try {
            Template template = newTemplateRequest.toEntity();
            Template updated = templateService.updateUserTemplate(templateId, userDetails.getId(), template);
            return ResponseEntity.ok(ApiResponse.success(updated.toDTO()));
        } catch (TemplateNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Template not found"));
        } catch (TemplateUpdateException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to update template"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTemplateById(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id) {
        try {
            templateService.deleteUserTemplateById(id, userDetails.getId());
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (TemplateNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Template not found"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to delete template"));
        }
    }

    // Template Exercise endpoints
    @GetMapping("/{templateId}/exercise")
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

    @PostMapping("/{templateId}/exercise")
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
        } catch (TemplateExerciseCreationException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to create template exercise"));
        }
    }

    @PutMapping("/{templateId}/exercise/{exerciseId}")
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
        } catch (TemplateExerciseCreationException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to update template exercise"));
        }
    }

    @DeleteMapping("/{templateId}/exercise/{exerciseId}")
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