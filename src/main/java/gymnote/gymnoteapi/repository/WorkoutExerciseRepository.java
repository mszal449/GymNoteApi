package gymnote.gymnoteapi.repository;

import gymnote.gymnoteapi.entity.WorkoutExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, Long> {
    List<WorkoutExercise> findByWorkoutIdOrderByExerciseOrder(Long workoutId);
    Optional<WorkoutExercise> findByIdAndWorkoutId(Long id, Long workoutId);


    @Query("SELECT MAX(we.exerciseOrder) FROM WorkoutExercise we WHERE we.workout.id = :workoutId")
    Optional<Integer> findMaxOrderByWorkoutId(@Param("workoutId") Long workoutId);

    @Query("UPDATE WorkoutExercise w SET w.exerciseOrder = w.exerciseOrder - 1 " +
            "WHERE w.workout.id = :workoutId AND w.exerciseOrder > :oldOrder AND w.exerciseOrder <= :newOrder")
    @Modifying
    void shiftOrdersUp(@Param("workoutId") Long workoutId,
                       @Param("oldOrder") int oldOrder,
                       @Param("newOrder") int newOrder);

    @Query("UPDATE WorkoutExercise w SET w.exerciseOrder = w.exerciseOrder + 1 " +
            "WHERE w.workout.id = :workoutId AND w.exerciseOrder >= :newOrder AND w.exerciseOrder < :oldOrder")
    @Modifying
    void shiftOrdersDown(@Param("workoutId") Long workoutId,
                         @Param("newOrder") int newOrder,
                         @Param("oldOrder") int oldOrder);

}