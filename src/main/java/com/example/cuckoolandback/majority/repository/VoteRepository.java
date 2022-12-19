package com.example.cuckoolandback.majority.repository;

import com.example.cuckoolandback.majority.domain.Vote;
import com.example.cuckoolandback.majority.domain.Vs;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote,Long> {
    //Optional<Vote> findByRoundNumAndRoomId(int roundNum, Long roomId);
}
