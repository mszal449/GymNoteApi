package gymnote.gymnoteapi.mapper;

import gymnote.gymnoteapi.entity.ExerciseSet;
import gymnote.gymnoteapi.model.dto.ExerciseSetDTO;
import gymnote.gymnoteapi.model.exerciseSet.CreateExerciseSetRequest;
import gymnote.gymnoteapi.model.exerciseSet.UpdateExerciseSetRequest;

public class ExerciseSetMapper {

    public static ExerciseSetDTO toDTO(ExerciseSet exerciseSet) {
        if (exerciseSet == null) return null;

        ExerciseSetDTO dto = new ExerciseSetDTO();
        dto.setId(exerciseSet.getId());
        dto.setWorkoutExerciseId(exerciseSet.getWorkoutExercise().getId());
        dto.setSetNumber(exerciseSet.getSetNumber());
        dto.setReps(exerciseSet.getReps());
        dto.setWeight(exerciseSet.getWeight());
        dto.setDuration(exerciseSet.getDuration());
        dto.setDistance(exerciseSet.getDistance());
        dto.setNotes(exerciseSet.getNotes());
        dto.setSetOrder(exerciseSet.getSetOrder());

        return dto;
    }

    public static ExerciseSet toEntity(CreateExerciseSetRequest request) {
        ExerciseSet set = new ExerciseSet();
        set.setSetNumber(request.getSetNumber());
        set.setReps(request.getReps());
        set.setWeight(request.getWeight());
        set.setDuration(request.getDuration());
        set.setDistance(request.getDistance());
        set.setNotes(request.getNotes());
        set.setSetOrder(request.getSetOrder());
        return set;
    }

    public static ExerciseSet toEntity(UpdateExerciseSetRequest request) {
        ExerciseSet set = new ExerciseSet();
        set.setSetNumber(request.getSetNumber());
        set.setReps(request.getReps());
        set.setWeight(request.getWeight());
        set.setDuration(request.getDuration());
        set.setDistance(request.getDistance());
        set.setNotes(request.getNotes());
        set.setSetOrder(request.getSetOrder());
        return set;
    }
}