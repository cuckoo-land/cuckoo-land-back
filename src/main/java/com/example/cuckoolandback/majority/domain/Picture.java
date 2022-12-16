package com.example.cuckoolandback.majority.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String url;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    int numOfWins;

    @Column(nullable = false)
    Long majorityId;
}
