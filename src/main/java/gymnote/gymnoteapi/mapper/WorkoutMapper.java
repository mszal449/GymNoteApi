package gymnote.gymnoteapi.mapper;

import gymnote.gymnoteapi.entity.Workout;
import gymnote.gymnoteapi.entity.WorkoutExercise;
import gymnote.gymnoteapi.model.dto.ExerciseSetDTO;
import gymnote.gymnoteapi.model.dto.WorkoutDTO;
import gymnote.gymnoteapi.model.dto.WorkoutExerciseDTO;
import gymnote.gymnoteapi.model.workout.CreateWorkoutRequest;
import gymnote.gymnoteapi.model.workout.UpdateWorkoutRequest;

import java.util.Comparator;
import java.util.stream.Collectors;


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

        if (workout.getWorkoutExercises() != null) {
            workoutDTO.setExercises(workout.getWorkoutExercises().stream()
                    .sorted(Comparator.comparing(WorkoutExercise::getExerciseOrder))
                    .map(workoutExercise -> {
                        WorkoutExerciseDTO exerciseDTO = new WorkoutExerciseDTO();
                        exerciseDTO.setId(workoutExercise.getId());
                        exerciseDTO.setExerciseId(workoutExercise.getExercise().getId());
                        exerciseDTO.setExerciseOrder(workoutExercise.getExerciseOrder());

                        if (workoutExercise.getSets() != null) {
                            exerciseDTO.setSets(workoutExercise.getSets().stream()
                                    .map(set -> {
                                        ExerciseSetDTO setDTO = new ExerciseSetDTO();
                                        setDTO.setId(set.getId());
                                        setDTO.setReps(set.getReps());
                                        setDTO.setWeight(set.getWeight());
                                        return setDTO;
                                    })
                                    .collect(Collectors.toList()));
                        }

                        return exerciseDTO;
                    })
                    .collect(Collectors.toList()));
        }

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
        workout.setName(workoutRequest.getName());
        workout.setStartTime(workoutRequest.getStartTime());
        workout.setEndTime(workoutRequest.getEndTime());
        workout.setNotes(workoutRequest.getNotes());
        return workout;
    }

    public static Workout toEntity(UpdateWorkoutRequest workoutRequest) {
        Workout workout = new Workout();
        workout.setName(workoutRequest.getName());
        workout.setStartTime(workoutRequest.getStartTime());
        workout.setEndTime(workoutRequest.getEndTime());
        workout.setNotes(workoutRequest.getNotes());
        return workout;
    }
}