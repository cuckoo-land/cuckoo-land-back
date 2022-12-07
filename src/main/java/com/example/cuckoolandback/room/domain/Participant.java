package com.example.cuckoolandback.room.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Participant {
    @Id
    String id;

    @Column(nullable = false)
    Long roomId;
}
