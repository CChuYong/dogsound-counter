package kr.swmaestro.dogsoundcounter.core.entities;

import lombok.Value;

@Value
public class User {
    private final Identity id;
    private final String username;
    private final String password;

    public static User newInstance(String username, String password){
        return new User(Identity.NOTHING, username, password);
    }
}
