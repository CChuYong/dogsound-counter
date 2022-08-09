package kr.swmaestro.dogsoundcounter.core.usecases;

import kr.swmaestro.dogsoundcounter.core.entities.DogSound;
import kr.swmaestro.dogsoundcounter.core.entities.User;
import kr.swmaestro.dogsoundcounter.infrastructure.jpa.entities.DogSoundData;
import kr.swmaestro.dogsoundcounter.infrastructure.jpa.entities.UserData;

import java.util.List;
import java.util.Optional;

public interface DogSoundRepository {
    DogSoundData persist(DogSound user);
    List<DogSoundData> findByRelations(UserData user);
    long countByRelations(UserData user);
}
