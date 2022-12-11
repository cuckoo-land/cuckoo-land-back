package com.example.cuckoolandback.user.repository;

import com.example.cuckoolandback.user.domain.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberId(String memberId);
    Optional<Member> findByNickname(String nickname);

    List<Member> findTopByMafiaWinScore();
    List<Member> findTopByMajorWinScore();
}
