package gymnote.gymnoteapi.model.templateExercise;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateTemplateExerciseRequest {
    @NotNull
    private Long templateId;

    @NotNull
    private Long exerciseId;

    @Min(value = 0, message = "Exercise order must be greater than or equal to 0")
    private Integer exerciseOrder;
}
