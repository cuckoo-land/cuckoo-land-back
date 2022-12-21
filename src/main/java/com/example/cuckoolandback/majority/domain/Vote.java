package com.example.cuckoolandback.majority.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    Long roomId;

    @Column(nullable = false)
    Long userId;

    @Column(nullable = false)
    Long pickId; //투표한 item아이디

    @Column(nullable = false)
    Long notPickId; //상대 item아이디

    @Column(nullable = false)
    boolean isAns;

}
