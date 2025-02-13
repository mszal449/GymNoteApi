package gymnote.gymnoteapi.mapper;

import gymnote.gymnoteapi.entity.Template;
import gymnote.gymnoteapi.model.template.CreateTemplateRequest;
import gymnote.gymnoteapi.model.template.UpdateTemplateRequest;

public class TemplateMapper {
    public static Template toEntity(CreateTemplateRequest createTemplateRequest) {
        Template template = new Template();
        template.setTemplateName(createTemplateRequest.getTemplateName());
        template.setDescription(createTemplateRequest.getDescription());
        return template;
    }

    public static Template toEntity(UpdateTemplateRequest updateTemplateRequest) {
        Template template = new Template();
        template.setTemplateName(updateTemplateRequest.getTemplateName());
        template.setDescription(updateTemplateRequest.getDescription());
        return template;
    }
}
