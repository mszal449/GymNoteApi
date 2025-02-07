package gymnote.gymnoteapi.repository;

import gymnote.gymnoteapi.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findByUserId(Long userId);
    Optional<Workout> findByIdAndUserId(Long id, Long userId);
    List<Workout> findByTemplateIdAndUserId(Long templateId, Long userId);

    @Query("SELECT w FROM Workout w " +
            "LEFT JOIN FETCH w.workoutExercises we " +
            "LEFT JOIN FETCH we.exercise " +
            "WHERE w.id = :workoutId AND w.user.id = :userId")
    Optional<Workout> findByIdAndUserIdWithExercises(@Param("workoutId") Long workoutId,
                                                     @Param("userId") Long userId);}
