package com.example.cuckoolandback.user.repository;

import com.example.cuckoolandback.user.domain.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {
//    @Query(value = "SELECT distinct memberId FROM Member\n" +
//        "WHERE roomId = :roomId",
//        nativeQuery = true)
//    List<Member>(@Param("roomId")Long roomId);
    Optional<Member> findByMemberId(String memberId);


    Optional<Member> findByNickname(String nickname);

    List<Member> findFirst10ByOrderByMafiaWinScoreDesc();

    List<Member> findFirst10ByOrderByMajorWinScoreDesc();
}
