package kr.swmaestro.dogsoundcounter.infrastructure.restapi.entities;

import lombok.Value;

@Value
public class SignUpRequest {
    private final String username;
    private final String password;
}
