package gymnote.gymnoteapi.entity;

import gymnote.gymnoteapi.model.dto.ExerciseDTO;
import gymnote.gymnoteapi.model.exercise.CreateExerciseRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "exercises")
@Getter
@Setter
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(nullable = false)
    private String exerciseName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EExerciseType type;

    private String description;

    private Integer orderIndex;

    public Exercise() {
    }

    public Exercise(CreateExerciseRequest createExerciseRequest) {
        this.exerciseName = createExerciseRequest.getExerciseName();
        this.type = EExerciseType.valueOf(createExerciseRequest.getType());
        this.description = createExerciseRequest.getDescription();
    }

    public Exercise(ExerciseDTO exerciseDTO) {
        this.exerciseName = exerciseDTO.getExerciseName();
        this.description = exerciseDTO.getDescription();
        this.type = EExerciseType.valueOf(exerciseDTO.getType());
    }

    public ExerciseDTO toDTO() {
        return new ExerciseDTO(this);
    }
}

