package kr.swmaestro.dogsoundcounter.infrastructure.jpa.repositories;

import kr.swmaestro.dogsoundcounter.core.entities.DogSound;
import kr.swmaestro.dogsoundcounter.core.usecases.DogSoundRepository;
import kr.swmaestro.dogsoundcounter.infrastructure.jpa.entities.DogSoundData;
import kr.swmaestro.dogsoundcounter.infrastructure.jpa.entities.UserData;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Repository
@AllArgsConstructor
public class DogSoundRepositoryImpl implements DogSoundRepository {
    private final JPADogSoundRepository repository;

    @Override
    public DogSoundData persist(DogSound user) {
        return repository.save(DogSoundData.fromEntity(user));
    }

    @Override
    public List<DogSoundData> findByRelations(UserData user) {
        final List<DogSoundData> myDogSounds = repository.findAllBySpeaker(user);
        final List<DogSoundData> theirDogSounds = repository.findAllByVictim(user);

        List<DogSoundData> combined = new ArrayList<>();
        combined.addAll(myDogSounds);
        combined.addAll(theirDogSounds);

        return combined.stream().sorted(Comparator.comparingLong(x -> {
            DogSoundData data = (DogSoundData) x;
            return data.getSpeakAt().toEpochMilli();
        }).reversed()).toList();
    }

    @Override
    public long countByRelations(UserData user) {
        List<DogSoundData> myDogSounds = this.findByRelations(user);
        return myDogSounds.stream()
                .filter(x -> !x.isPaid())
                .map(x -> x.getSpeaker().getId() == user.getId() ? x.getPrice() : -x.getPrice())
                .reduce(0, Integer::sum);
    }

    @Override
    public List<DogSoundData> findByRelationsGreaterThan(UserData user, long id) {
        return repository.findAllByVictimAndIdGreaterThanOrSpeakerAndIdGreaterThanOrderByIdDesc(user,id, user, id);
    }

}
