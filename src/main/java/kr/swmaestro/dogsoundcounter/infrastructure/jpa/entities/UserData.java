package kr.swmaestro.dogsoundcounter.infrastructure.jpa.entities;

import kr.swmaestro.dogsoundcounter.core.entities.Identity;
import kr.swmaestro.dogsoundcounter.core.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

import static kr.swmaestro.dogsoundcounter.infrastructure.jpa.entities.IdentityMapper.toIdentityValue;

@Entity(name = "user")
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Data
public class UserData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;

    public static UserData fromEntity(User user){
        return new UserData(
                toIdentityValue(user.getId()),
                user.getUsername(),
                user.getPassword()
                );
    }

    public User toEntity(){
        return new User(
                new Identity(this.id),
                this.username,
                this.password
        );
    }
}
