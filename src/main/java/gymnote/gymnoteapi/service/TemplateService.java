package gymnote.gymnoteapi.service;

import gymnote.gymnoteapi.entity.Template;
import gymnote.gymnoteapi.entity.User;
import gymnote.gymnoteapi.model.dto.TemplateDTO;
import gymnote.gymnoteapi.repository.TemplateRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TemplateService {
    private final TemplateRepository templateRepository;
    private final UserService userService;

    public List<TemplateDTO> getTemplatesByUserId(Long userId) {
        return templateRepository.findByUserId(userId).stream().map(TemplateDTO::new).toList();
    }

    public Optional<Template> getTemplateById(Long id) {
        return templateRepository.findById(id);
    }

    public Template getUserTemplateById(Long id, Long userId) {
        return templateRepository.findByIdAndUserId(id, userId).orElse(null);
    }

    public Optional<Template> createTemplate(Template template, Long userId) {
        Optional<User> user = userService.findById(userId);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        template.setUser(userService.findById(userId).get());
        return Optional.of(templateRepository.save(template));
    }

    public Optional<Template> updateTemplate(Template template, Long userId) {
        Optional<Template> existingTemplate = templateRepository.findByIdAndUserId(template.getId(), userId);
        if (existingTemplate.isEmpty()) {
            throw new IllegalArgumentException("Template not found");
        }
        if (template.getTemplateName() != null) {
            existingTemplate.get().setTemplateName(template.getTemplateName());
        }
        if (template.getDescription() != null) {
            existingTemplate.get().setDescription(template.getDescription());
        }
        return Optional.of(templateRepository.save(existingTemplate.get()));
    }

    @Transactional
    public void deleteUserTemplateById(Long id, Long userId) throws IllegalArgumentException{
        Optional<Template> template = templateRepository.findByIdAndUserId(id, userId);
        if (template.isEmpty()) {
            throw new IllegalArgumentException("Template not found");
        }
        templateRepository.delete(template.get());
    }
}
