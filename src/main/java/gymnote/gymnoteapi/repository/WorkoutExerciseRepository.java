package gymnote.gymnoteapi.repository;

import gymnote.gymnoteapi.entity.WorkoutExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, Long> {
    List<WorkoutExercise> findByWorkoutIdOrderByExerciseOrder(Long workoutId);
    Optional<WorkoutExercise> findByIdAndWorkoutId(Long id, Long workoutId);

    @Query("SELECT MAX(we.exerciseOrder) FROM WorkoutExercise we WHERE we.workout.id = :workoutId")
    Optional<Integer> findMaxOrderByWorkoutId(@Param("workoutId") Long workoutId);
}