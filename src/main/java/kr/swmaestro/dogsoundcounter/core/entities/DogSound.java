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
    private final boolean paid;
    private final int price;

    public static DogSound newInstance(User speaker, User victim, String content, Instant speakAt, int price){
        return new DogSound(Identity.NOTHING, speaker, victim, content, speakAt, false, price);
    }
}
