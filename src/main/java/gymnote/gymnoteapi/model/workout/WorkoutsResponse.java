package gymnote.gymnoteapi.model.workout;

import gymnote.gymnoteapi.model.dto.WorkoutDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WorkoutsResponse {
    private Integer count;
    private List<WorkoutDTO> workouts;
}
