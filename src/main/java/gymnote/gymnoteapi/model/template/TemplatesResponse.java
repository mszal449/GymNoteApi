package gymnote.gymnoteapi.model.template;

import gymnote.gymnoteapi.model.dto.TemplateDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TemplatesResponse {
    private List<TemplateDTO> templates;
    private Integer count;
}
