package gymnote.gymnoteapi.repository;

import java.util.Optional;

import gymnote.gymnoteapi.entity.RefreshToken;
import gymnote.gymnoteapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;


@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    int deleteByUser(User user);

    Optional<RefreshToken> findByUserId(Long userId);

}