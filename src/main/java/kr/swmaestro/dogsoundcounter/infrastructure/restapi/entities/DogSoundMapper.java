package kr.swmaestro.dogsoundcounter.infrastructure.restapi.entities;

import kr.swmaestro.dogsoundcounter.core.entities.DogSound;

public class DogSoundMapper {
    public static DogSoundResponse map(DogSound data){
        return new DogSoundResponse(data.getContent(), data.getSpeaker().getUsername(), data.getVictim().getUsername(), data.getSpeakAt());
    }
}
