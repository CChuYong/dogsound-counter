package kr.swmaestro.dogsoundcounter.infrastructure.restapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.swmaestro.dogsoundcounter.core.entities.DogSound;
import kr.swmaestro.dogsoundcounter.core.usecases.DogSoundRepository;
import kr.swmaestro.dogsoundcounter.core.usecases.UserRepository;
import kr.swmaestro.dogsoundcounter.infrastructure.firebase.NotificationService;
import kr.swmaestro.dogsoundcounter.infrastructure.jpa.entities.DogSoundData;
import kr.swmaestro.dogsoundcounter.infrastructure.jpa.entities.UserData;
import kr.swmaestro.dogsoundcounter.infrastructure.restapi.entities.*;
import kr.swmaestro.dogsoundcounter.util.events.EventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/data")
@RequiredArgsConstructor
public class DogDataController {
    private final UserRepository repository;
    private final DogSoundRepository dataRepository;

    private final NotificationService notificationService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final EventHandler eventHandler;

    @PostMapping
    CompletableFuture<ApiResponse> post(@RequestBody DogDataInsertRequest request) {
        if (request.getContent() == null || request.getTarget() == null)
            return CompletableFuture.completedFuture(ApiResponse.error("필요한 값이 부족합니다"));
        final UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        final String from = token.getName();
        return CompletableFuture.supplyAsync(() -> {
            final Optional<UserData> fromUser = repository.findByUsername(from);
            final Optional<UserData> toUser = repository.findByUsername(request.getTarget());
            if (toUser.isEmpty()) return ApiResponse.error("해당 유저를 찾을 수 없습니다");
            if (fromUser.isEmpty()) return ApiResponse.error("세션이 올바르지 않습니다");

            DogDataInsertEvent event = new DogDataInsertEvent(fromUser.get(), toUser.get(), request.getContent(), 100);
            eventHandler.invoke(event);

            if(event.isCancelled()) return ApiResponse.error("취소되었습니다.");

            final DogSound sound = DogSound.newInstance(event.getTo().toEntity(), event.getFrom().toEntity(), event.getContent(), Instant.now(), event.getPrice());
            final DogSoundData data = dataRepository.persist(sound);

            CompletableFuture
                    .supplyAsync(() -> notificationService.sendNotification(toUser.get(), event.getPrice() + "원어치 개소리를 하셨습니다", request.getContent()));

            if (data.getId() != null && data.getId() > 0)
                return ApiResponse.succeed("성공적으로 " + event.getPrice() + "원짜리 개소리를 등록했습니다!");
            else
                return ApiResponse.error("알 수 없는 이유로 등록에 실패했습니다");
        });
    }

    @GetMapping(value = "/{id}")
    CompletableFuture<List<DogSoundResponse>> get(@PathVariable("id") long id) {
        final UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        final String from = token.getName();
        return CompletableFuture.supplyAsync(() -> {
            final Optional<UserData> fromUser = repository.findByUsername(from);
            if (fromUser.isEmpty()) return Collections.emptyList();
            final List<DogSoundData> myDogSounds = dataRepository.findByRelationsGreaterThan(fromUser.get(), id);
            return myDogSounds.stream().map(DogSoundData::toEntity).map(DogSoundMapper::map).toList();
        });
    }

    @GetMapping(value = "/toPay")
    CompletableFuture<ApiResponse> getToPay() {
        final UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        final String from = token.getName();
        return CompletableFuture.supplyAsync(() -> {
            final Optional<UserData> fromUser = repository.findByUsername(from);
            if (fromUser.isEmpty()) return ApiResponse.succeed("0");
            final long count = dataRepository.countByRelations(fromUser.get());
            return ApiResponse.succeed(Long.toString(count));
        });
    }

    @GetMapping
    CompletableFuture<List<DogSoundResponse>> get() {
        final UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        final String from = token.getName();
        return CompletableFuture.supplyAsync(() -> {
            final Optional<UserData> fromUser = repository.findByUsername(from);
            if (fromUser.isEmpty()) return Collections.emptyList();
            final List<DogSoundData> myDogSounds = dataRepository.findByRelations(fromUser.get());
            return myDogSounds.stream().map(DogSoundData::toEntity).map(DogSoundMapper::map).toList();
        });
    }


//    @PostMapping(value = "/pay")
//    CompletableFuture<ApiResponse> pay(){
//        final UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
//        final String userName = token.getName();
//        return CompletableFuture.supplyAsync(()->{
//            final Optional<UserData> user = repository.findByUsername(userName);
//            if(user.isEmpty()) return ApiResponse.succeed("0");
//
//
//            return ApiResponse.succeed(Long.toString(count));
//        });
//    }
}
