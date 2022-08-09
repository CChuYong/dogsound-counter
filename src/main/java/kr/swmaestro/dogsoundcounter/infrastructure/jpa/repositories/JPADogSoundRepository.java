package kr.swmaestro.dogsoundcounter.infrastructure.jpa.repositories;

import kr.swmaestro.dogsoundcounter.infrastructure.jpa.entities.DogSoundData;
import kr.swmaestro.dogsoundcounter.infrastructure.jpa.entities.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JPADogSoundRepository extends JpaRepository<DogSoundData, Long> {
    List<DogSoundData> findAllByVictim(UserData user);
    List<DogSoundData> findAllBySpeaker(UserData user);
    Long countByVictim(UserData user);
    Long countBySpeaker(UserData user);
}
