    package gymnote.gymnoteapi.entity;


    import gymnote.gymnoteapi.model.dto.TemplateExerciseDTO;
    import jakarta.persistence.*;
    import lombok.Getter;
    import lombok.Setter;

    import java.util.Date;

    @Entity
    @Table(name = "template_exercises", uniqueConstraints = {
            @UniqueConstraint(columnNames = {"template_id", "exercise_order"})
    })
    @Getter
    @Setter
    public class TemplateExercise {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "template_id", nullable = false)
        private Template template;

        @ManyToOne
        @JoinColumn(name = "exercise_id", nullable = false)
        private Exercise exercise;

        @Column(nullable = false)
        private Integer exerciseOrder;

        @Temporal(TemporalType.TIMESTAMP)
        private Date createdAt;

        public TemplateExerciseDTO toDTO() {
            TemplateExerciseDTO templateExerciseDTO = new TemplateExerciseDTO();
            templateExerciseDTO.setId(id);
            templateExerciseDTO.setExerciseId(exercise.getId());
            templateExerciseDTO.setExerciseOrder(exerciseOrder);

            return templateExerciseDTO;
        }
    }