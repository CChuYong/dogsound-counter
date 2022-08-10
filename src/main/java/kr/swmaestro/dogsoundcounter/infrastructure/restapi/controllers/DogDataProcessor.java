package kr.swmaestro.dogsoundcounter.infrastructure.restapi.controllers;

import kr.swmaestro.dogsoundcounter.infrastructure.restapi.entities.DogDataInsertEvent;
import kr.swmaestro.dogsoundcounter.util.events.EventHandler;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class DogDataProcessor {
    private final EventHandler eventHandler;
    public DogDataProcessor(EventHandler eventHandler){
        this.eventHandler = eventHandler;
        eventHandler.subscribe(DogDataInsertEvent.class, this::onDogDataInsertEvent);
    }

    private void onDogDataInsertEvent(DogDataInsertEvent event) {
        if (event.getContent().contains("자살")) event.setPrice(500);
        if (new Random().nextInt(100) <= 3) event.setPrice(1000);
    }
}
