package gymnote.gymnoteapi.model.dto;

import gymnote.gymnoteapi.entity.Workout;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class WorkoutDTO {
    private Long id;
    private Long userId;
    private Long templateId;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String notes;
//    private List<WorkoutExerciseDTO> workoutExercises;

}