package kr.swmaestro.dogsoundcounter.core.entities;

import lombok.Value;

import java.time.Instant;

@Value
public class DogSound {
    private final Identity identity;
    private final User speaker;
    private final User victim;
    private final String content;
    private final Instant speakAt;

    public static DogSound newInstance(User speaker, User victim, String content, Instant speakAt){
        return new DogSound(Identity.NOTHING, speaker, victim, content, speakAt);
    }
}
