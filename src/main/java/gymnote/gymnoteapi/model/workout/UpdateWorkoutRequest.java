package gymnote.gymnoteapi.model.workout;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateWorkoutRequest {
    private Long id;
    private Long userId;
    private Long templateId;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String notes;
}
