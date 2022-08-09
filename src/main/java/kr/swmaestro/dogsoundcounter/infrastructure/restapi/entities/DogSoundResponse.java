package kr.swmaestro.dogsoundcounter.infrastructure.restapi.entities;

import lombok.Value;

import java.time.Instant;

@Value
public class DogSoundResponse {
    private final String content;
    private final String speaker;
    private final String victim;
    private final Instant speakAt;
}
