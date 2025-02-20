package gymnote.gymnoteapi.repository;

import gymnote.gymnoteapi.entity.TemplateExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TemplateExerciseRepository extends JpaRepository<TemplateExercise, Long> {
    Optional<TemplateExercise> findByIdAndTemplateId(Long id, Long userId);

    @Query("SELECT te FROM TemplateExercise te LEFT JOIN FETCH te.exercise WHERE te.template.id = :templateId AND te.template.user.id = :userId")
    List<TemplateExercise> findByTemplateIdAndTemplateUserIdWithExercises(@Param("templateId") Long templateId, @Param("userId") Long userId);

}
