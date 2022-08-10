package kr.swmaestro.dogsoundcounter.infrastructure.restapi.entities;

import kr.swmaestro.dogsoundcounter.infrastructure.jpa.entities.UserData;
import kr.swmaestro.dogsoundcounter.util.events.Cancellable;
import kr.swmaestro.dogsoundcounter.util.events.Event;
import lombok.Data;

@Data
public class DogDataInsertEvent extends Event implements Cancellable {
    private final UserData from;
    private final UserData to;
    private String content;
    private int price;

    public DogDataInsertEvent(UserData from, UserData to, String content, int price) {
        this.from = from;
        this.to = to;
        this.content = content;
        this.price = price;
    }
    private boolean cancelled;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
