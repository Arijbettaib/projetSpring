package ma.ensa.authservice.repository;

import ma.ensa.authservice.entity.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppRoleRepo extends JpaRepository<AppRole, Integer> {
    Optional<AppRole> findByName(String name);

}
