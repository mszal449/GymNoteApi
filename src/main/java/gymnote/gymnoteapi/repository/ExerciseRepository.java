package gymnote.gymnoteapi.repository;

import gymnote.gymnoteapi.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    Optional<Exercise> findByIdAndUserId(Long id, Long templateId);

    List<Exercise> findByUserId(Long id);
}
