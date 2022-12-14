package com.example.cuckoolandback.room.domain;

import com.example.cuckoolandback.mafia.Player;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    String code;

    @Column(nullable = false)
    String hostId;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    GameType type;

    @Column(nullable = false)
    boolean visibility;

    @Column(nullable = false)
    int maximum;

    @Enumerated(EnumType.ORDINAL)
    @ColumnDefault("0")
    @Column(nullable = false)
    RoomStatus state;

    @Column(nullable = true)
    String password;

    @Column
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Player> playerList;

}
