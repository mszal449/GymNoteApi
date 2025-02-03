package gymnote.gymnoteapi.repository;

import gymnote.gymnoteapi.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TemplateRepository extends JpaRepository<Template, Long> {
    List<Template> findByUserId(Long id);

    Optional<Template> findByIdAndUserId(Long id, Long userId);

    Optional<Template> deleteByIdAndUserId(Long id, Long userId);
}
