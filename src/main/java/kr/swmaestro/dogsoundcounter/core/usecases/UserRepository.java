package kr.swmaestro.dogsoundcounter.core.usecases;

import kr.swmaestro.dogsoundcounter.core.entities.User;
import kr.swmaestro.dogsoundcounter.infrastructure.jpa.entities.UserData;

import java.util.Optional;

public interface UserRepository {
    UserData persist(User user);

    Optional<UserData> findByUsername(String username);
}
