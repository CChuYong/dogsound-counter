package kr.swmaestro.dogsoundcounter.repositories;

import kr.swmaestro.dogsoundcounter.core.entities.User;
import kr.swmaestro.dogsoundcounter.infrastructure.jpa.entities.UserData;
import kr.swmaestro.dogsoundcounter.infrastructure.jpa.repositories.JPAUserRepository;
import kr.swmaestro.dogsoundcounter.infrastructure.jpa.repositories.UserRepositoryImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    JPAUserRepository repository;

    @Autowired
    UserRepositoryImpl userRepository;

    @EntityScan("kr.swmaestro.dogsoundcounter.infrastructure.jpa.entities")
    @Configuration
    @AutoConfigurationPackage
    static class Config{

    }

    @Test
    public void createNewUserAndFlush() throws Exception{
        final User user = User.newInstance("umjunsick", "fighting");
        final UserData data = userRepository.persist(user);
        assertThat(data.getId() > 0).isTrue();
    }
}
