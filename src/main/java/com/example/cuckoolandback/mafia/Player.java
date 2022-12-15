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
    private String memberId;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private boolean isDead;

    @Column
    public Long roomId;

    @Column
    private boolean haveRight;

    @Column
    private int missionCnt;

    @Column
    private int voteCnt;

    public Player(Participant participant){
        this.memberId = participant.getId();
        this.roomId = participant.getRoomId();
        this.isDead = false;
        this.haveRight = false;
        this.missionCnt = 0;
        this.voteCnt = 0;
    }
}
