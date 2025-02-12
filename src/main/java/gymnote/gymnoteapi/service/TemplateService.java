package gymnote.gymnoteapi.service;

import gymnote.gymnoteapi.entity.Template;
import gymnote.gymnoteapi.entity.User;
import gymnote.gymnoteapi.exception.template.TemplateCreationException;
import gymnote.gymnoteapi.exception.template.TemplateDeletionException;
import gymnote.gymnoteapi.exception.template.TemplateNotFoundException;
import gymnote.gymnoteapi.exception.template.TemplateUpdateException;
import gymnote.gymnoteapi.repository.TemplateRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TemplateService {
    private final TemplateRepository templateRepository;
    private final UserService userService;

    public List<Template> getTemplatesByUserId(Long userId) {
        return templateRepository.findByUserId(userId);
    }

    public Template getUserTemplateById(Long templateId, Long userId) {
        return templateRepository.findByIdAndUserId(templateId, userId)
                .orElseThrow(() -> new TemplateNotFoundException("Template not found with id: " + templateId + " for user: " + userId));
    }


    public Template createUserTemplate(Template template, Long userId) {
        User user = userService.findById(userId);
        template.setUser(user);

        try{
            return templateRepository.save(template);
        } catch (DataIntegrityViolationException e) {
            throw new TemplateCreationException("Failed to create template due to data integrity violation", e);
        } catch (Exception e) {
            throw new TemplateCreationException("Failed to create template", e);
        }
    }

    public Template updateUserTemplate(Long templateId, Long userId, Template templateData) {
        Template template = templateRepository.findByIdAndUserId(templateId, userId)
                        .orElseThrow(() -> new TemplateNotFoundException(
                                "Template not found with id: " + templateData.getId() + " for user: " + userId
                        ));

        updateTemplateFields(template, templateData);

        try {
            return templateRepository.save(template);
        } catch (DataIntegrityViolationException e) {
            throw new TemplateUpdateException("Failed to update exercise due to data integrity violation", e);
        } catch (Exception e) {
            throw new TemplateUpdateException("Failed to update exercise", e);
        }
    }

    @Transactional
    public void deleteUserTemplateById(Long id, Long userId) {
        Template template = templateRepository.findByIdAndUserId(id, userId)
                        .orElseThrow(() -> new TemplateNotFoundException(
                                "Template not found with id: " + id + " for user: " + userId
                        ));

        try {
            templateRepository.delete(template);
        } catch (Exception e) {
            throw new TemplateDeletionException("Failed to delete template", e);
        }
    }

    private void updateTemplateFields(Template existingTemplate, Template template) {
        if (template.getTemplateName() != null) {
            existingTemplate.setTemplateName(template.getTemplateName());
        }

        if (template.getDescription() != null) {
            existingTemplate.setDescription(template.getDescription());
        }
    }
}
