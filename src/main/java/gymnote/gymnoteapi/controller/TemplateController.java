package gymnote.gymnoteapi.controller;

import gymnote.gymnoteapi.entity.Template;
import gymnote.gymnoteapi.entity.TemplateExercise;
import gymnote.gymnoteapi.exception.UserNotFoundException;
import gymnote.gymnoteapi.exception.template.TemplateCreationException;
import gymnote.gymnoteapi.exception.template.TemplateNotFoundException;
import gymnote.gymnoteapi.exception.template.TemplateUpdateException;
import gymnote.gymnoteapi.exception.templateExercise.TemplateExerciseCreationException;
import gymnote.gymnoteapi.exception.templateExercise.TemplateExerciseNotFoundException;
import gymnote.gymnoteapi.model.dto.TemplateDTO;
import gymnote.gymnoteapi.model.dto.TemplateExerciseDTO;
import gymnote.gymnoteapi.model.template.*;
import gymnote.gymnoteapi.model.templateExercise.CreateTemplateExerciseRequest;
import gymnote.gymnoteapi.model.templateExercise.TemplateExercisesResponse;
import gymnote.gymnoteapi.security.service.UserDetailsImpl;
import gymnote.gymnoteapi.service.TemplateExerciseService;
import gymnote.gymnoteapi.service.TemplateService;
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
@RequestMapping("/api/template")
public class TemplateController {
    public final TemplateService templateService;
    public final TemplateExerciseService templateExerciseService;

    // TEMPLATE ENDPOINTS

    @GetMapping
    public ResponseEntity<TemplatesResponse> getUserTemplates(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<Template> templates = templateService.getTemplatesByUserId(userDetails.getId());

        TemplatesResponse templatesResponse = new TemplatesResponse();
        templatesResponse.setTemplates(templates.stream().map(TemplateDTO::new).toList());
        templatesResponse.setCount(templates.size());

        return ResponseEntity.ok(templatesResponse);
    }


    @GetMapping("/{id}")
    public ResponseEntity<TemplateDTO> getUserTemplateById(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id) {
        try {
            Template template = templateService.getUserTemplateById(id, userDetails.getId());
            return ResponseEntity.ok(template.toDTO());
        } catch (TemplateNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<TemplateDTO> createTemplate(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CreateTemplateRequest createTemplateRequest) {
        Template template = createTemplateRequest.toEntity();

        try {
            Template createdTemplate = templateService.createUserTemplate(template, userDetails.getId());
            return ResponseEntity.ok(createdTemplate.toDTO());
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (TemplateCreationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{templateId}")
    public ResponseEntity<TemplateDTO> updateTemplate(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long templateId,
            @RequestBody UpdateTemplateRequest newTemplateRequest) {
        Template template = newTemplateRequest.toEntity();

        try {
            Template updatedTemplate = templateService.updateUserTemplate(templateId, userDetails.getId(), template);
            return ResponseEntity.ok(updatedTemplate.toDTO());
        } catch (TemplateNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (TemplateUpdateException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTemplateById(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id) {
        try {
            templateService.deleteUserTemplateById(id, userDetails.getId());
            return ResponseEntity.noContent().build();
        } catch (TemplateNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    // TEMPLATE EXERCISE ENDPOINTS

    @GetMapping("/{templateId}/exercise")
    public ResponseEntity<TemplateExercisesResponse> getTemplateExercises(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long templateId) {
        try {
            List<TemplateExercise> template = templateExerciseService.getUserTemplateExercises(templateId, userDetails.getId());

            TemplateExercisesResponse templateExercisesResponse = new TemplateExercisesResponse();
            templateExercisesResponse.setTemplateId(templateId);
            templateExercisesResponse.setTemplateExercises(
                    template.stream()
                            .map(TemplateExerciseDTO::new)
                            .toList());
            templateExercisesResponse.setCount(template.size());

            return ResponseEntity.ok(templateExercisesResponse);
        } catch (TemplateNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{templateId}/exercise")
    public ResponseEntity<TemplateExerciseDTO> addTemplateExercise(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long templateId,
            @RequestBody CreateTemplateExerciseRequest createTemplateExerciseRequest) {
        try {
            TemplateExercise templateExerciseData = createTemplateExerciseRequest.toEntity();

            TemplateExercise createdExercise = templateExerciseService.addUserTemplateExercise(
                    templateId,
                    userDetails.getId(),
                    templateExerciseData
            );

            return ResponseEntity.ok(createdExercise.toDTO());
        } catch (TemplateNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (TemplateExerciseCreationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{templateId}/exercise/{exerciseId}")
    public ResponseEntity<TemplateExerciseDTO> updateTemplateExercise(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long templateId,
            @PathVariable Long exerciseId,
            @RequestBody CreateTemplateExerciseRequest createTemplateExerciseRequest) {
        try {
            TemplateExercise templateExerciseData = createTemplateExerciseRequest.toEntity();

            TemplateExercise createdExercise = templateExerciseService.updateUserTemplateExercise(
                    exerciseId,
                    templateId,
                    userDetails.getId(),
                    templateExerciseData
            );

            return ResponseEntity.ok(createdExercise.toDTO());
        } catch (TemplateNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (TemplateExerciseCreationException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @DeleteMapping("/{templateId}/exercise/{exerciseId}")
    public ResponseEntity<?> deleteTemplateExercise(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long templateId,
            @PathVariable Long exerciseId) {
        try {
            templateExerciseService.deleteUserTemplateExercise(
                    exerciseId,
                    templateId,
                    userDetails.getId()
            );

            return ResponseEntity.noContent().build();
        } catch (TemplateNotFoundException | TemplateExerciseNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
