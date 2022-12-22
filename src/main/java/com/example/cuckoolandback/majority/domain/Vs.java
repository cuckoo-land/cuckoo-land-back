package com.example.cuckoolandback.majority.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Vs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    Long roomId;

    @Column(nullable = false)
    int roundNum;

    @Column(nullable = false)
    Long itemId1;

    @Column(nullable = false)
    Long itemId2;

    @Column(nullable = false)
    Long winner;

    @Column(nullable = false)
    int numOfVote1;
    @Column(nullable = false)
    int numOfVote2;
    @Column(nullable = false)
    int numOfTotal;
}
