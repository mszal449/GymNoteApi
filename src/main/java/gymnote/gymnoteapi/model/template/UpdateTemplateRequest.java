package gymnote.gymnoteapi.model.template;

import gymnote.gymnoteapi.entity.Template;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTemplateRequest {
    private String templateName;
    private String description;

    public Template toEntity() {
        Template template = new Template();
        template.setTemplateName(templateName);
        template.setDescription(description);
        return template;
    }
}
