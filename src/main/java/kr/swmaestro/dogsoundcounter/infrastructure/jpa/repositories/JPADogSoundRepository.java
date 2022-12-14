package kr.swmaestro.dogsoundcounter.infrastructure.jpa.repositories;

import kr.swmaestro.dogsoundcounter.infrastructure.jpa.entities.DogSoundData;
import kr.swmaestro.dogsoundcounter.infrastructure.jpa.entities.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JPADogSoundRepository extends JpaRepository<DogSoundData, Long> {
    List<DogSoundData> findAllByVictim(UserData user);

    List<DogSoundData> findAllBySpeaker(UserData user);

    List<DogSoundData> findAllByVictimOrSpeakerOrderByIdDesc(UserData victim, UserData speaker);

    Long countByVictim(UserData user);

    Long countBySpeaker(UserData user);

    List<DogSoundData> findAllByVictimAndIdGreaterThan(UserData user, Long id);

    List<DogSoundData> findAllBySpeakerAndIdGreaterThan(UserData user, Long id);

    List<DogSoundData> findAllByVictimAndIdGreaterThanOrSpeakerAndIdGreaterThanOrderByIdDesc(UserData victim, Long id, UserData speaker, Long id2);
}
