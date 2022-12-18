package com.example.cuckoolandback.room.domain;

import com.example.cuckoolandback.common.domain.BaseTime;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Participant extends BaseTime {
    @Id
    String id; // memberId

    @Column(nullable = false)
    Long roomId;

    @Column(nullable = false)
    boolean hostTF;
}
