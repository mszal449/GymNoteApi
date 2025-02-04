package gymnote.gymnoteapi.service;

import gymnote.gymnoteapi.entity.EExerciseType;
import gymnote.gymnoteapi.entity.Exercise;
import gymnote.gymnoteapi.entity.User;
import gymnote.gymnoteapi.model.dto.ExerciseDTO;
import gymnote.gymnoteapi.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final UserService userService;

    public Optional<ExerciseDTO> getUserExerciseById(Long exerciseId, Long userId) {
        return exerciseRepository.findByIdAndUserId(exerciseId, userId).map(ExerciseDTO::new);
    }

    public Optional<ExerciseDTO> createExercise(ExerciseDTO exercise, Long userId) {
        Optional<User> foundUser = userService.findById(userId);
        if (foundUser.isEmpty()) {
            return Optional.empty();
        }
        User user = foundUser.get();

        Exercise newExercise = new Exercise(exercise);
        newExercise.setUser(user);

        Exercise resultExercise = exerciseRepository.save(newExercise);
        return Optional.of(new ExerciseDTO(resultExercise));
    }

    public Optional<ExerciseDTO> updateExercise(Long exerciseId, Long userId, ExerciseDTO updateExerciseRequest) {
        Optional<Exercise> foundExercise = exerciseRepository.findByIdAndUserId(exerciseId, userId);
        if (foundExercise.isEmpty()) {
            return Optional.empty();
        }

        Exercise exercise = foundExercise.get();
        if (updateExerciseRequest.getExerciseName() != null) {
            exercise.setExerciseName(updateExerciseRequest.getExerciseName());
        }
        if (updateExerciseRequest.getType() != null) {
            exercise.setType(EExerciseType.valueOf(updateExerciseRequest.getType()));
        }
        if (updateExerciseRequest.getDescription() != null) {
            exercise.setDescription(updateExerciseRequest.getDescription());
        }
        if (updateExerciseRequest.getOrderIndex() != null) {
            exercise.setOrderIndex(updateExerciseRequest.getOrderIndex());
        }

        Exercise resultExercise = exerciseRepository.save(exercise);
        return Optional.of(new ExerciseDTO(resultExercise));
    }

    public boolean deleteExercise(Long exerciseId, Long userId) {
        Optional<Exercise> foundExercise = exerciseRepository.findByIdAndUserId(exerciseId, userId);
        if (foundExercise.isEmpty()) {
            return false;
        }
        exerciseRepository.delete(foundExercise.get());
        return true;
    }
}
