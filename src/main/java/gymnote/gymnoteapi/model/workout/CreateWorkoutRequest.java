package gymnote.gymnoteapi.model.workout;

import gymnote.gymnoteapi.entity.Workout;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateWorkoutRequest {
    private Long templateId;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String notes;

    public Workout toEntity() {
        Workout workout = new Workout();
        workout.setName(this.name);
        workout.setStartTime(this.startTime);
        workout.setEndTime(this.endTime);
        workout.setNotes(this.notes);
        // Note: user and template should be set separately in the service layer
        return workout;
    }
}
