package kr.swmaestro.dogsoundcounter.infrastructure.security.services;

import kr.swmaestro.dogsoundcounter.core.entities.User;
import kr.swmaestro.dogsoundcounter.core.usecases.UserRepository;
import kr.swmaestro.dogsoundcounter.infrastructure.security.entities.PrincipalDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PrincipalDetailService implements UserDetailsService {
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다")).toEntity();
        return new PrincipalDetails(user);
    }
}
