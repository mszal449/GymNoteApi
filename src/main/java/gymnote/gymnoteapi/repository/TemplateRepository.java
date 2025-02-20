package gymnote.gymnoteapi.repository;

import gymnote.gymnoteapi.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TemplateRepository extends JpaRepository<Template, Long> {
    List<Template> findByUserId(Long id);

    Optional<Template> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT t FROM Template t LEFT JOIN FETCH t.templateExercises te LEFT JOIN FETCH te.exercise WHERE t.id = :id AND t.user.id = :userId")
    Optional<Template> findByIdAndUserIdWithExercises(@Param("id") Long id, @Param("userId") Long userId);


    Optional<Template> deleteByIdAndUserId(Long id, Long userId);
}
