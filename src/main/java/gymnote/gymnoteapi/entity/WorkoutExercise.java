package gymnote.gymnoteapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "workout_exercises",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"workout_id", "exercise_order"})
})
@Getter
@Setter
public class WorkoutExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout;

    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable  = false)
    private Exercise exercise;

    @Column(nullable = false)
    private Integer realOrder;

//    @OneToMany(mappedBy = "workoutExercise", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ExerciseSet> sets;
}