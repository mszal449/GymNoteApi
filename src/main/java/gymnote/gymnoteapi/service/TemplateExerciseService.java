package gymnote.gymnoteapi.service;

import gymnote.gymnoteapi.entity.Template;
import gymnote.gymnoteapi.model.dto.TemplateDTO;
import gymnote.gymnoteapi.model.dto.TemplateExerciseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TemplateExerciseService {
    private final TemplateService templateService;

    public Optional<Template> addUserTemplateExercise(
            Long templateId,
            Long userId,
            TemplateExerciseDTO templateExerciseDTO) {

        Template template = templateService.getUserTemplateById(templateId, userId);


    }
}
