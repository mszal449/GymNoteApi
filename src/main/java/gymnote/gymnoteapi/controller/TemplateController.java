package gymnote.gymnoteapi.controller;

import gymnote.gymnoteapi.entity.Template;
import gymnote.gymnoteapi.model.dto.TemplateDTO;
import gymnote.gymnoteapi.model.jwt.MessageResponse;
import gymnote.gymnoteapi.model.template.NewTemplateRequest;
import gymnote.gymnoteapi.model.template.TemplateResponse;
import gymnote.gymnoteapi.model.template.TemplatesResponse;
import gymnote.gymnoteapi.security.service.UserDetailsImpl;
import gymnote.gymnoteapi.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
@RequestMapping("/api/template")
public class TemplateController {
    public final TemplateService templateService;

    @GetMapping
    public ResponseEntity<TemplatesResponse> getUserTemplates(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<Template> templates = templateService.getTemplatesByUserId(userDetails.getId());
        TemplatesResponse templatesResponse = new TemplatesResponse();
        List<TemplateDTO> templateDTOS = templates.stream().map(TemplateDTO::new).toList();
        templatesResponse.setTemplates(templateDTOS);
        templatesResponse.setCount(templates.size());
        return ResponseEntity.ok(templatesResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TemplateResponse> getUserTemplateById(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id
    ) {
        Template template = templateService.getUserTemplateById(id, userDetails.getId());
        if (template == null) {
            return ResponseEntity.notFound().build();
        }

        TemplateResponse templateResponse = new TemplateResponse();
        TemplateDTO templateDTO = new TemplateDTO(template);
        templateResponse.setTemplate(templateDTO);
        return ResponseEntity.ok(templateResponse);
    }

    @PostMapping
    public ResponseEntity<TemplateResponse> createTemplate(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody NewTemplateRequest newTemplateRequest
    ) {
        Template template = new Template();
        template.setTemplateName(newTemplateRequest.getTemplateName());
        template.setDescription(newTemplateRequest.getDescription());

        try {
            Optional<Template> createdTemplate = templateService.createTemplate(template, userDetails.getId());
            if (createdTemplate.isEmpty()) {
                throw new Exception("Failed to create template");
            }
            TemplateResponse templateResponse = new TemplateResponse();
            TemplateDTO templateDTO = new TemplateDTO(createdTemplate.get());
            templateResponse.setTemplate(templateDTO);
            return ResponseEntity.ok(templateResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TemplateResponse> updateTemplate(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id,
            @RequestBody NewTemplateRequest newTemplateRequest
    ) {
        Template template = new Template();
        template.setId(id);
        template.setTemplateName(newTemplateRequest.getTemplateName());
        template.setDescription(newTemplateRequest.getDescription());

        try {
            Optional<Template> updatedTemplate = templateService.updateTemplate(template, userDetails.getId());
            if (updatedTemplate.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            TemplateResponse templateResponse = new TemplateResponse();
            TemplateDTO templateDTO = new TemplateDTO(updatedTemplate.get());
            templateResponse.setTemplate(templateDTO);
            return ResponseEntity.ok(templateResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteTemplateById(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id
    ) {
        try {
            templateService.deleteUserTemplateById(id, userDetails.getId());
            return ResponseEntity.ok(new MessageResponse("Template deleted successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(new MessageResponse("Template not found"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
