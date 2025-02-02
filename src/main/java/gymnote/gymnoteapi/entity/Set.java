package gymnote.gymnoteapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Sets")
@Getter
@Setter
public class Set {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long setId;

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

    @NotBlank
    private Integer setOrder;
}