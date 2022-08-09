package kr.swmaestro.dogsoundcounter.infrastructure.jpa.entities;

import kr.swmaestro.dogsoundcounter.core.entities.DogSound;
import kr.swmaestro.dogsoundcounter.core.entities.Identity;
import kr.swmaestro.dogsoundcounter.core.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.Instant;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class DogSoundData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="speaker_id", nullable = false)
    private UserData speaker;

    @ManyToOne
    @JoinColumn(name="victim_id", nullable = false)
    private UserData victim;

    @Column(length = 1024, nullable = false)
    private String content;

    @Column(name = "speak_at", nullable = false)
    private Instant speakAt;

    public static DogSoundData fromEntity(DogSound data){
        return new DogSoundData(data.getIdentity().getId(),
                UserData.fromEntity(data.getSpeaker()),
                UserData.fromEntity(data.getVictim()),
                data.getContent(),
                data.getSpeakAt());
    }

    public DogSound toEntity(){
        return new DogSound(new Identity(this.id),
                speaker.toEntity(),
                victim.toEntity(),
                content,
                speakAt);
    }
}
