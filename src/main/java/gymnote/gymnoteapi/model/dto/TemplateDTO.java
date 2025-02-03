package gymnote.gymnoteapi.model.dto;

import gymnote.gymnoteapi.entity.Template;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemplateDTO {
    private Long id;
    private String name;
    private String description;
    private Long ownerId;

    public TemplateDTO(Template template) {
        this.id = template.getId();
        this.name = template.getTemplateName();
        this.description = template.getDescription();
        this.ownerId = template.getUser().getId();
    }
}
