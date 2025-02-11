package gymnote.gymnoteapi.repository;

import gymnote.gymnoteapi.entity.ExerciseSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ExerciseSetRepository extends JpaRepository<ExerciseSet, Long> {
    @Query("""
        SELECT es FROM ExerciseSet es 
        JOIN es.workoutExercise we 
        JOIN we.workout w 
        WHERE we.id = :exerciseId 
        AND w.id = :workoutId 
        AND w.user.id = :userId 
        ORDER BY es.setOrder
    """)
    List<ExerciseSet> findAllValidated(
            Long exerciseId,
            Long workoutId,
            Long userId
    );

    @Query("""
        SELECT es FROM ExerciseSet es 
        JOIN es.workoutExercise we 
        JOIN we.workout w 
        WHERE es.id = :setId 
        AND we.id = :exerciseId 
        AND w.id = :workoutId 
        AND w.user.id = :userId
    """)
    Optional<ExerciseSet> findOneValidated(
            Long setId,
            Long exerciseId,
            Long workoutId,
            Long userId
    );

    @Query("""
        SELECT es FROM ExerciseSet es 
        JOIN es.workoutExercise we 
        JOIN we.workout w 
        WHERE we.id = :exerciseId 
        AND w.id = :workoutId 
        AND w.user.id = :userId 
        ORDER BY es.setOrder
    """)
    List<ExerciseSet> findSetsForReordering(Long exerciseId, Long workoutId, Long userId);

    @Modifying
    @Query("""
        UPDATE ExerciseSet es 
        SET es.setOrder = :newOrder 
        WHERE es.id = :setId
    """)
    void updateSetOrder(Long setId, Integer newOrder);

    @Modifying
    @Query("""
        DELETE FROM ExerciseSet es 
        WHERE es.id = :setId 
        AND es.workoutExercise.id = :exerciseId 
        AND es.workoutExercise.workout.id = :workoutId 
        AND es.workoutExercise.workout.user.id = :userId
    """)
    int deleteValidated(Long setId, Long exerciseId, Long workoutId, Long userId);

    @Modifying
    @Query("""
        UPDATE ExerciseSet es 
        SET es.setOrder = es.setOrder - 1 
        WHERE es.workoutExercise.id = :exerciseId 
        AND es.workoutExercise.workout.id = :workoutId 
        AND es.workoutExercise.workout.user.id = :userId 
        AND es.setOrder > :deletedOrder
    """)
    void reorderAfterDelete(Long exerciseId, Long workoutId, Long userId, Integer deletedOrder);


}