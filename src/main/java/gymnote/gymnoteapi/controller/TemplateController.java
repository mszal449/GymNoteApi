package gymnote.gymnoteapi.controller;

import gymnote.gymnoteapi.entity.Template;
import gymnote.gymnoteapi.mapper.TemplateMapper;
import gymnote.gymnoteapi.model.api.ApiResponse;
import gymnote.gymnoteapi.model.dto.TemplateDTO;
import gymnote.gymnoteapi.model.template.CreateTemplateRequest;
import gymnote.gymnoteapi.model.template.UpdateTemplateRequest;
import gymnote.gymnoteapi.security.service.UserDetailsImpl;
import gymnote.gymnoteapi.service.TemplateService;
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
@RequestMapping("/api/template")
public class TemplateController {
    private final TemplateService templateService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TemplateDTO>>> getUserTemplates(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<Template> templates = templateService.getTemplatesByUserId(userDetails.getId());
        List<TemplateDTO> templateDTOs = templates.stream()
            .map(Template::toDTO)
            .toList();

        return ResponseEntity.ok(ApiResponse.success(templateDTOs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TemplateDTO>> getUserTemplateById(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @PathVariable Long id) {
        Template template = templateService.getUserTemplateById(id, userDetails.getId());

        return ResponseEntity.ok(ApiResponse.success(template.toDTO()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TemplateDTO>> createTemplate(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody CreateTemplateRequest createTemplateRequest) {
        Template template = TemplateMapper.toEntity(createTemplateRequest);
        Template created = templateService.createUserTemplate(template, userDetails.getId());

        return ResponseEntity.ok(ApiResponse.success(created.toDTO()));
    }

    @PutMapping("/{templateId}")
    public ResponseEntity<ApiResponse<TemplateDTO>> updateTemplate(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @PathVariable Long templateId,
            @Valid @RequestBody UpdateTemplateRequest updateTemplateRequest) {
        Template template = TemplateMapper.toEntity(updateTemplateRequest);
        Template updated = templateService.updateUserTemplate(templateId, userDetails.getId(), template);

        return ResponseEntity.ok(ApiResponse.success(updated.toDTO()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTemplateById(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @PathVariable Long id) {
        templateService.deleteUserTemplateById(id, userDetails.getId());

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}