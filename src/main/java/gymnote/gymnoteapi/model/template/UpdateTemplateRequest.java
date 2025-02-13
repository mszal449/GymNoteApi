package gymnote.gymnoteapi.model.template;

import gymnote.gymnoteapi.entity.Template;
import lombok.Data;

@Data
public class UpdateTemplateRequest {
    private String templateName;
    private String description;

    // TODO: remove
    public Template toEntity() {
        Template template = new Template();
        template.setTemplateName(templateName);
        template.setDescription(description);
        return template;
    }
}
