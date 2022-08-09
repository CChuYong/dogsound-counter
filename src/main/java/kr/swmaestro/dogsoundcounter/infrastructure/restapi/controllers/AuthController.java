package kr.swmaestro.dogsoundcounter.infrastructure.restapi.controllers;

import kr.swmaestro.dogsoundcounter.core.entities.User;
import kr.swmaestro.dogsoundcounter.core.usecases.UserRepository;
import kr.swmaestro.dogsoundcounter.infrastructure.jpa.entities.UserData;
import kr.swmaestro.dogsoundcounter.infrastructure.security.TokenUtils;
import kr.swmaestro.dogsoundcounter.infrastructure.restapi.entities.ApiResponse;
import kr.swmaestro.dogsoundcounter.infrastructure.restapi.entities.LoginRequest;
import kr.swmaestro.dogsoundcounter.infrastructure.restapi.entities.SignUpRequest;
import kr.swmaestro.dogsoundcounter.infrastructure.restapi.entities.SignUpRequestMapper;
import kr.swmaestro.dogsoundcounter.infrastructure.security.entities.PrincipalDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private UserRepository userRepository;
    private SignUpRequestMapper signUpRequestMapper;
    private AuthenticationManager authManager;

    @PostMapping(value = "/register")
    CompletableFuture<ApiResponse> register(@RequestBody SignUpRequest signUpRequest) {
        if(signUpRequest.getUsername() == null || signUpRequest.getUsername().length() < 4) return CompletableFuture.completedFuture(ApiResponse.error("아이디는 4자리 이상으로 설정해주세요"));
        if(signUpRequest.getPassword() == null || signUpRequest.getPassword().length() < 4) return CompletableFuture.completedFuture(ApiResponse.error("비밀번호는 4자리 이상으로 설정해주세요"));
        final User user = signUpRequestMapper.map(signUpRequest);
        return CompletableFuture.supplyAsync(() -> {
            final Optional<UserData> lastUser = userRepository.findByUsername(user.getUsername());
            if(lastUser.isPresent()) return ApiResponse.error("같은 닉네임을 가진 유저가 이미 존재합니다");
            Long transactionId = userRepository.persist(user).getId();
            if(transactionId == null || transactionId < 0){
                return ApiResponse.error("알 수 없는 오류가 발생하였습니다.");
            }
            return ApiResponse.succeed("회원가입에 성공했습니다");
        });
    }

    @PostMapping(value="/login")
    CompletableFuture<ApiResponse> register(@RequestBody LoginRequest loginRequest){
        if(loginRequest.getUsername() == null || loginRequest.getUsername().length() < 4) return CompletableFuture.completedFuture(ApiResponse.error("입력 정보 오류"));
        if(loginRequest.getPassword() == null || loginRequest.getPassword().length() < 4) return CompletableFuture.completedFuture(ApiResponse.error("입력 정보 오류"));
        return CompletableFuture.supplyAsync(()->{
            try{
                Authentication token = authManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(token);

                PrincipalDetails details = (PrincipalDetails) token.getPrincipal();
                String jwtToken = TokenUtils.generateJwtToken(details.getUser());
                return ApiResponse.succeed(jwtToken);
            }catch(BadCredentialsException exception){
                return ApiResponse.error("로그인 실패");
            }
        });
    }
}
