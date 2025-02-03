package gymnote.gymnoteapi.model.template;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NewTemplateRequest {
    private String templateName;
    private String description;
}
