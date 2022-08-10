package kr.swmaestro.dogsoundcounter.infrastructure.jpa.repositories;

import kr.swmaestro.dogsoundcounter.core.entities.User;
import kr.swmaestro.dogsoundcounter.core.usecases.UserRepository;
import kr.swmaestro.dogsoundcounter.infrastructure.jpa.entities.UserData;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final JPAUserRepository repository;

    public UserRepositoryImpl(JPAUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserData persist(User user) {
        final UserData userData = UserData.fromEntity(user);
        return repository.save(userData);
    }

    @Override
    public Optional<UserData> findByUsername(String username) {
        return repository.findByUsername(username);
    }
}
