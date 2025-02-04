package gymnote.gymnoteapi.entity;

import gymnote.gymnoteapi.model.dto.ExerciseDTO;
import gymnote.gymnoteapi.model.exercise.NewExerciseRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Exercises")
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

    public Exercise(NewExerciseRequest newExerciseRequest) {
        this.exerciseName = newExerciseRequest.getExerciseName();
        this.type = EExerciseType.valueOf(newExerciseRequest.getType());
        this.description = newExerciseRequest.getDescription();
        this.orderIndex = newExerciseRequest.getOrderIndex();
    }

    public Exercise(ExerciseDTO exerciseDTO) {
        this.exerciseName = exerciseDTO.getExerciseName();
        this.description = exerciseDTO.getDescription();
        this.orderIndex = exerciseDTO.getOrderIndex();
        this.type = EExerciseType.valueOf(exerciseDTO.getType());
    }
}

