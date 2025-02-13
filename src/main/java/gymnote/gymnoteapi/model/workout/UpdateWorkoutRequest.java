package gymnote.gymnoteapi.model.workout;

import gymnote.gymnoteapi.entity.Workout;
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

    // TODO: remove
    public Workout toEntity() {
        Workout workout = new Workout();
        workout.setId(this.id);
        workout.setName(this.name);
        workout.setStartTime(this.startTime);
        workout.setEndTime(this.endTime);
        workout.setNotes(this.notes);
        return workout;
    }
}
