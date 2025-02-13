package gymnote.gymnoteapi.model.workout;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateWorkoutRequest {
    private Long templateId;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String notes;
}
