package gymnote.gymnoteapi.entity;

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

    @Column(nullable = false)
    private String exerciseName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExerciseType type;

    private String description;
}

enum ExerciseType {
    REPS, DURATION, DISTANCE
}