package gymnote.gymnoteapi.repository;

import gymnote.gymnoteapi.entity.TemplateExercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TemplateExerciseRepository extends JpaRepository<TemplateExercise, Long> {
    Optional<TemplateExercise> findByIdAndTemplateId(Long id, Long userId);
}
