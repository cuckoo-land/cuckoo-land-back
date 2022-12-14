package com.example.cuckoolandback.mafia;

import com.example.cuckoolandback.room.domain.Room;
import com.example.cuckoolandback.user.domain.Member;
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

    @OneToOne
    private Member member;

    @Column
    private Role role;

    @Column
    private boolean isDead;

    @ManyToOne
    @JoinColumn(name = "room_Id")
    public Room room;

    @Column
    private int right;

    @Column
    private int missionCnt;
}
