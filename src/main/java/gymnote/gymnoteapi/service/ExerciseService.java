package gymnote.gymnoteapi.service;

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

    public Optional<ExerciseDTO> createExercise(Exercise exercise, Long userId) {
        Optional<User> foundUser = userService.findById(userId);
        if (foundUser.isEmpty()) {
            return Optional.empty();
        }
        User user = foundUser.get();

        exercise.setUser(user);
        Exercise newExercise = exerciseRepository.save(exercise);
        return Optional.of(new ExerciseDTO(newExercise));
    }

}
