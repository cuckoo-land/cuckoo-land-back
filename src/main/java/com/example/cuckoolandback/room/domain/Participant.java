package com.example.cuckoolandback.room.domain;

import com.example.cuckoolandback.user.domain.Member;
import lombok.*;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Participant {
    @Id
    String id; // memberId

    @Column(nullable = false)
    Long roomId;

    @Column(nullable = false)
    boolean hostTF;
}
