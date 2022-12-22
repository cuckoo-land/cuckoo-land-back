package com.example.cuckoolandback.majority.repository;

import com.example.cuckoolandback.majority.domain.Vs;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VsRepository extends JpaRepository<Vs,Long> {
    Optional<Vs> findByRoundNumAndRoomId(int roundNum, Long roomId);
    List<Vs> findTop3ByRoomIdOrderByWinnerRate(Long roomId);
}
