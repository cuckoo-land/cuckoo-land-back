package com.example.cuckoolandback.mafia.repository;

import com.example.cuckoolandback.mafia.domain.Role;
import com.example.cuckoolandback.mafia.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player,Long> {
    Optional<Player> findByMemberId(String memberId);
    List<Player> findByRoleAndRoomId(Role role, Long roomId);
    List<Player> findByRoomId(Long roomId);
}
