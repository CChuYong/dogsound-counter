package kr.swmaestro.dogsoundcounter.infrastructure.restapi.entities;

import lombok.Value;

@Value
public class DogDataInsertRequest {
    private final String target;
    private final String content;
}
