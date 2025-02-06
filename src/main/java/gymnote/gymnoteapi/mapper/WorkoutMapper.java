package gymnote.gymnoteapi.mapper;

import gymnote.gymnoteapi.entity.Workout;
import gymnote.gymnoteapi.model.dto.WorkoutDTO;
import gymnote.gymnoteapi.model.workout.CreateWorkoutRequest;

public class WorkoutMapper {
    public static WorkoutDTO toDTO(Workout workout) {
        WorkoutDTO workoutDTO = new WorkoutDTO();
        workoutDTO.setId(workout.getId());
        workoutDTO.setUserId(workout.getUser().getId());
        workoutDTO.setTemplateId(workout.getTemplate() != null ? workout.getTemplate().getId() : null);
        workoutDTO.setName(workout.getName());
        workoutDTO.setStartTime(workout.getStartTime());
        workoutDTO.setEndTime(workout.getEndTime());
        workoutDTO.setNotes(workout.getNotes());
        // TODO: Maybe Add mapping for workoutExercises if needed
        return workoutDTO;
    }

    public static Workout toEntity(WorkoutDTO workoutDTO) {
        Workout workout = new Workout();
        workout.setId(workoutDTO.getId());
        workout.setName(workoutDTO.getName());
        workout.setStartTime(workoutDTO.getStartTime());
        workout.setEndTime(workoutDTO.getEndTime());
        workout.setNotes(workoutDTO.getNotes());
        return workout;
    }

    public static Workout toEntity(CreateWorkoutRequest workoutRequest) {
        Workout workout = new Workout();
        workout.setId(workoutRequest.getId());
        workout.setName(workoutRequest.getName());
        workout.setStartTime(workoutRequest.getStartTime());
        workout.setEndTime(workoutRequest.getEndTime());
        workout.setNotes(workoutRequest.getNotes());
        return workout;
    }
}
