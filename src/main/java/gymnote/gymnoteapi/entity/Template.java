package gymnote.gymnoteapi.entity;

import gymnote.gymnoteapi.exception.templateExercise.TemplateExerciseIllegalOrderIdException;
import gymnote.gymnoteapi.model.dto.TemplateDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "templates")
@Getter
@Setter
public class Template {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String templateName;

    private String description;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TemplateExercise> templateExercises = new ArrayList<>();

    @Getter
    @Setter
    @OneToMany(mappedBy = "template")
    private Collection<Workout> workout;

    public TemplateDTO toDTO() {
        TemplateDTO templateDTO = new TemplateDTO();
        templateDTO.setId(id);
        templateDTO.setName(templateName);
        templateDTO.setDescription(description);
        templateDTO.setOwnerId(user.getId());
        templateDTO.setExercises(templateExercises.stream().map(TemplateExercise::toDTO).toList());

        return templateDTO;
    }
}