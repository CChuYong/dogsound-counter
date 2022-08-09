package kr.swmaestro.dogsoundcounter.infrastructure.restapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import kr.swmaestro.dogsoundcounter.core.entities.DogSound;
import kr.swmaestro.dogsoundcounter.core.usecases.DogSoundRepository;
import kr.swmaestro.dogsoundcounter.core.usecases.UserRepository;
import kr.swmaestro.dogsoundcounter.infrastructure.jpa.entities.DogSoundData;
import kr.swmaestro.dogsoundcounter.infrastructure.jpa.entities.UserData;
import kr.swmaestro.dogsoundcounter.infrastructure.restapi.entities.*;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/data")
@AllArgsConstructor
public class DogDataController {
    private UserRepository repository;
    private DogSoundRepository dataRepository;
    @PostMapping
    CompletableFuture<ApiResponse> post(@RequestBody DogDataInsertRequest request) {
        if(request.getContent() == null || request.getTarget() == null) return CompletableFuture.completedFuture(ApiResponse.error("필요한 값이 부족합니다"));
        final UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        final String from = token.getName();
        return CompletableFuture.supplyAsync(()->{
            final Optional<UserData> fromUser = repository.findByUsername(from);
            final Optional<UserData> toUser = repository.findByUsername(request.getTarget());
            if(toUser.isEmpty()) return ApiResponse.error("해당 유저를 찾을 수 없습니다");
            if(fromUser.isEmpty()) return ApiResponse.error("세션이 올바르지 않습니다");
            if(fromUser.get().getId() == toUser.get().getId()) return ApiResponse.error("자기 자신에게 등록할 수 없습니다");
            final DogSound sound = DogSound.newInstance(toUser.get().toEntity(), fromUser.get().toEntity(), request.getContent(), Instant.now());
            final DogSoundData data = dataRepository.persist(sound);
            if(data.getId() != null && data.getId() > 0)
                return ApiResponse.succeed("성공적으로 개소리를 등록했습니다!");
            else
                return ApiResponse.error("알 수 없는 이유로 등록에 실패했습니다");
        });
    }

    @GetMapping
    CompletableFuture<List<DogSoundResponse>> get(){
        final UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        final String from = token.getName();
        return CompletableFuture.supplyAsync(()->{
            final Optional<UserData> fromUser = repository.findByUsername(from);
            if(fromUser.isEmpty()) return Collections.emptyList();
            final List<DogSoundData> myDogSounds = dataRepository.findByRelations(fromUser.get());
            return myDogSounds.stream().map(DogSoundData::toEntity).map(DogSoundMapper::map).toList();
        });
    }

    @GetMapping(value = "/toPay")
    CompletableFuture<ApiResponse> getToPay(){
        final UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        final String from = token.getName();
        return CompletableFuture.supplyAsync(()->{
            final Optional<UserData> fromUser = repository.findByUsername(from);
            if(fromUser.isEmpty()) return ApiResponse.succeed("0");
            final long count = dataRepository.countByRelations(fromUser.get());
            return ApiResponse.succeed(Long.toString(count));
        });
    }
}
