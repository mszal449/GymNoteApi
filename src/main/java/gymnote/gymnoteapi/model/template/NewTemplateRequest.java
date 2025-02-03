package gymnote.gymnoteapi.model.template;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewTemplateRequest {
    private String templateName;
    private String description;
}
