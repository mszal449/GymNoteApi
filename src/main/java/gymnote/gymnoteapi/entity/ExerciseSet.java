package gymnote.gymnoteapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "exercise_sets")
@Getter
@Setter
public class ExerciseSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "workout_exercise_id", nullable = false)
    private WorkoutExercise workoutExercise;

    @Column(nullable = false)
    private Integer setNumber;

    private Integer reps;
    private Double weight;
    private Integer duration;   // in seconds
    private Double distance;    // in meters
    private String notes;

    @Min(0)
    private Integer setOrder;
}