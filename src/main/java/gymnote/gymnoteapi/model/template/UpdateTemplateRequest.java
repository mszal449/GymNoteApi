package gymnote.gymnoteapi.model.template;

import lombok.Data;

@Data
public class UpdateTemplateRequest {
    private String templateName;
    private String description;
}
