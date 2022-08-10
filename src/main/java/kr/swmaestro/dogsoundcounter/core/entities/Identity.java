package kr.swmaestro.dogsoundcounter.core.entities;

import lombok.Value;

@Value
public class Identity {
    public static Identity NOTHING = new Identity(Long.MIN_VALUE);
    private final Long id;
}
