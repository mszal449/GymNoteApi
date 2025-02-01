package gymnote.gymnoteapi.repository;
import java.util.Optional;

import gymnote.gymnoteapi.entity.ERole;
import gymnote.gymnoteapi.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
