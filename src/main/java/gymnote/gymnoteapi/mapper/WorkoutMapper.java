package gymnote.gymnoteapi.mapper;

import gymnote.gymnoteapi.entity.Workout;
import gymnote.gymnoteapi.entity.WorkoutExercise;
import gymnote.gymnoteapi.model.dto.WorkoutDTO;
import gymnote.gymnoteapi.model.dto.WorkoutExerciseDTO;
import gymnote.gymnoteapi.model.workout.CreateWorkoutRequest;

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

        // Map workout exercises if they exist
        if (workout.getWorkoutExercises() != null) {
            workoutDTO.setExercises(workout.getWorkoutExercises().stream()
                    .sorted(Comparator.comparing(WorkoutExercise::getRealOrder))
                    .map(workoutExercise -> {
                        WorkoutExerciseDTO exerciseDTO = new WorkoutExerciseDTO();
                        exerciseDTO.setId(workoutExercise.getId());
                        exerciseDTO.setExerciseId(workoutExercise.getExercise().getId());
                        exerciseDTO.setRealOrder(workoutExercise.getRealOrder());

//                        // Map sets if they exist
//                        if (workoutExercise.getSets() != null) {
//                            exerciseDTO.setSets(workoutExercise.getSets().stream()
//                                    .map(set -> {
//                                        ExerciseSetDTO setDTO = new ExerciseSetDTO();
//                                        setDTO.setId(set.getId());
//                                        setDTO.setReps(set.getReps());
//                                        setDTO.setWeight(set.getWeight());
//                                        // Add other set properties as needed
//                                        return setDTO;
//                                    })
//                                    .collect(Collectors.toList()));
//                        }

                        return exerciseDTO;
                    })
                    .collect(Collectors.toList()));
        }

        return workoutDTO;
    }

    // Your existing toEntity methods remain the same
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
}