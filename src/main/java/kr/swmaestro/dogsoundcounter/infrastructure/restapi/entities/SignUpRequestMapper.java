package kr.swmaestro.dogsoundcounter.infrastructure.restapi.entities;

import kr.swmaestro.dogsoundcounter.core.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SignUpRequestMapper {
    private PasswordEncoder encoder;
    public User map(SignUpRequest request){
        return User.newInstance(request.getUsername(), encoder.encode(request.getPassword()));
    }
}
