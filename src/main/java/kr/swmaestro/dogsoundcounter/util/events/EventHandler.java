package kr.swmaestro.dogsoundcounter.util.events;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

@Component
public class EventHandler {
    private final HashMap<Class<? extends Event>, List<Consumer<? extends Event>>> handlerList = new HashMap<>();

    public synchronized <T extends Event> void subscribe(Class<T> eventType, Consumer<T> handler) {
        if (!handlerList.containsKey(eventType)) {
            handlerList.put(eventType, new ArrayList<>());
        }
        handlerList.get(eventType).add(handler);
    }

    @SuppressWarnings("unchecked")
    public <T extends Event> void invoke(T event) {
        List<Consumer<? extends Event>> eventList = handlerList.get(event.getClass());
        if (eventList != null) {
            eventList.forEach(consumer -> ((Consumer<T>) consumer).accept(event));
        }
    }
}
