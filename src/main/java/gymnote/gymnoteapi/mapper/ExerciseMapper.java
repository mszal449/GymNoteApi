package gymnote.gymnoteapi.mapper;

import gymnote.gymnoteapi.entity.EExerciseType;
import gymnote.gymnoteapi.entity.Exercise;
import gymnote.gymnoteapi.model.dto.ExerciseDTO;
import gymnote.gymnoteapi.model.exercise.CreateExerciseRequest;
import gymnote.gymnoteapi.model.exercise.UpdateExerciseRequest;

public class ExerciseMapper {
    public static Exercise toEntity(CreateExerciseRequest request) {
        Exercise exercise = new Exercise();
        exercise.setExerciseName(request.getExerciseName());
        exercise.setDescription(request.getDescription());
        exercise.setType(EExerciseType.valueOf(request.getType()));
        return exercise;
    }

    public static Exercise toEntity(UpdateExerciseRequest request) {
        Exercise exercise = new Exercise();
        exercise.setExerciseName(request.getExerciseName());
        exercise.setDescription(request.getDescription());
        exercise.setType(EExerciseType.valueOf(request.getType()));
        exercise.setOrderIndex(request.getOrderIndex());
        return exercise;
    }

    public static ExerciseDTO toDTO(Exercise exercise) {
        ExerciseDTO response = new ExerciseDTO();
        response.setId(exercise.getId());
        response.setExerciseName(exercise.getExerciseName());
        response.setDescription(exercise.getDescription());
        response.setType(exercise.getType().toString());
        response.setUserId(exercise.getUser().getId());
        return response;
    }
}
