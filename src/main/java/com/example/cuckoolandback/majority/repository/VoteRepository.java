package com.example.cuckoolandback.majority.repository;

import com.example.cuckoolandback.majority.domain.Vote;
import com.example.cuckoolandback.majority.domain.Vs;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VoteRepository extends JpaRepository<Vote,Long> {
    @Query(value = "SELECT COUNT(*) FROM Vote\n" +
        "WHERE roomId = :roomId and memberId=:memberId and isAns=:1",
        nativeQuery = true)
    int numOfWin(@Param("roomId")Long roomId,@Param("memberId")Long memberId);
    //Optional<Vote> findByRoundNumAndRoomId(int roundNum, Long roomId);
}
