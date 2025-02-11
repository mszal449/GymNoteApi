package gymnote.gymnoteapi.mapper;

import gymnote.gymnoteapi.entity.WorkoutExercise;
import gymnote.gymnoteapi.model.dto.WorkoutExerciseDTO;
import gymnote.gymnoteapi.model.workoutExercise.CreateWorkoutExerciseRequest;

public class WorkoutExerciseMapper {
    public static WorkoutExerciseDTO toDTO(WorkoutExercise workoutExercise) {
        if (workoutExercise == null) {
            return null;
        }

        WorkoutExerciseDTO dto = new WorkoutExerciseDTO();
        dto.setId(workoutExercise.getId());
        dto.setWorkoutId(workoutExercise.getWorkout().getId());
        dto.setExerciseId(workoutExercise.getExercise().getId());
        dto.setExerciseOrder(workoutExercise.getExerciseOrder());

        return dto;
    }

    public static WorkoutExercise toEntity(CreateWorkoutExerciseRequest request) {
        WorkoutExercise workoutExercise = new WorkoutExercise();
        workoutExercise.setExerciseOrder(request.getExerciseOrder());
        return workoutExercise;
    }
}