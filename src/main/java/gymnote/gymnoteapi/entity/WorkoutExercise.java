package gymnote.gymnoteapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "workout_exercises", uniqueConstraints = {
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
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Column(name = "exerciseOrder", nullable = false, updatable = false)
    private Integer exerciseOrder;

    @ManyToOne
    @JoinColumn(name = "template_exercise_id")
    private TemplateExercise templateExercise;

    @OneToMany(mappedBy = "workoutExercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExerciseSet> sets;

    public void initializeFromTemplateExercise(TemplateExercise templateExercise) {
        this.templateExercise = templateExercise;
        this.exercise = templateExercise.getExercise();
        this.exerciseOrder = templateExercise.getExerciseOrder();
    }
}