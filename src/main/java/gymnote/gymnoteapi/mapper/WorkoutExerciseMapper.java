package gymnote.gymnoteapi.mapper;

import gymnote.gymnoteapi.entity.WorkoutExercise;
import gymnote.gymnoteapi.model.dto.WorkoutExerciseDTO;

public class WorkoutExerciseMapper {
    public static WorkoutExerciseDTO toDTO(WorkoutExercise workoutExercise) {
        if (workoutExercise == null) {
            return null;
        }

        WorkoutExerciseDTO dto = new WorkoutExerciseDTO();
        dto.setId(workoutExercise.getId());
        dto.setWorkoutId(workoutExercise.getWorkout().getId());
        dto.setExerciseId(workoutExercise.getExercise().getId());
        dto.setExerciseName(workoutExercise.getExercise().getExerciseName());
        dto.setExerciseOrder(workoutExercise.getExerciseOrder());

        return dto;
    }



}