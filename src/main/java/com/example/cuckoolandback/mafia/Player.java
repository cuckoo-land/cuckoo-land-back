package com.example.cuckoolandback.mafia;

import com.example.cuckoolandback.room.domain.Participant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Player {

    @Id
    private Long id;

    @Column
    private String memberId;

    @Column
    private Role role;

    @Column
    private boolean isDead;

    @Column
    public Long roomId;

    @Column
    private boolean haveRight;

    @Column
    private int missionCnt;

    public Player(Participant participant){
        this.memberId = participant.getId();
        this.roomId = participant.getRoomId();
        this.isDead = false;
        this.haveRight = true;
        this.missionCnt = 0;
    }
}
