package com.example.cuckoolandback.mafia;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player,Long> {
    Optional<Player> findByMemberId(String memberId);
    List<Player> findByRoleAndRoomId(Role role, Long roomId);
    List<Player> findByRoomId(Long roomId);
}
