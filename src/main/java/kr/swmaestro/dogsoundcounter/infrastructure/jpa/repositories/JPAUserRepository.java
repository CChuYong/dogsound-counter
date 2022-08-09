package kr.swmaestro.dogsoundcounter.infrastructure.jpa.repositories;

import kr.swmaestro.dogsoundcounter.infrastructure.jpa.entities.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JPAUserRepository extends JpaRepository<UserData, Long> {
    boolean existsByUsername(String username);
    Optional<UserData> findByUsername(String username);
}
