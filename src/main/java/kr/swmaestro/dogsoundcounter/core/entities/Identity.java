package kr.swmaestro.dogsoundcounter.core.entities;

import lombok.Value;

@Value
public class Identity {
    private final Long id;
    public static Identity NOTHING = new Identity(Long.MIN_VALUE);
}
