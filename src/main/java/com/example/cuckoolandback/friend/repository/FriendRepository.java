package com.example.cuckoolandback.friend.repository;

import com.example.cuckoolandback.friend.domain.Friend;
import com.example.cuckoolandback.user.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    List<Friend> findAllByRequest(Member member);
    Optional<Friend> findByRequestAndResponse(Member request, Member response);
}
