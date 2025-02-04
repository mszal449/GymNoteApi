package gymnote.gymnoteapi.model.dto;

import gymnote.gymnoteapi.entity.Template;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TemplateDTO {
    private Long id;
    private String name;
    private String description;
    private Long ownerId;
    private List<TemplateExerciseDTO> exercises;

    public TemplateDTO(Template template) {
        this.id = template.getId();
        this.name = template.getTemplateName();
        this.description = template.getDescription();
        this.ownerId = template.getUser().getId();
        this.exercises = template.getTemplateExercises().stream().map(TemplateExerciseDTO::new).toList();
    }
}
