package gymnote.gymnoteapi.model.template;

import gymnote.gymnoteapi.entity.Template;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateTemplateRequest {
    @NotBlank
    @Size(max=100, message = "Template name must be less than 100 characters")
    private String templateName;

    @Size(max=1000, message = "Template description must be less than 100 characters")
    private String description;

    public Template toEntity() {
        Template template = new Template();
        template.setTemplateName(templateName);
        template.setDescription(description);
        return template;
    }
}
