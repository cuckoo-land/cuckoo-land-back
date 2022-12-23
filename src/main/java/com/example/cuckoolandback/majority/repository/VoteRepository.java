package com.example.cuckoolandback.majority.repository;

import com.example.cuckoolandback.majority.domain.Vote;
import com.example.cuckoolandback.majority.domain.Vs;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VoteRepository extends JpaRepository<Vote,Long> {
    @Query(value = "SELECT *, COUNT(case when isAns=1 then 1 end) as result FROM Vote\n " +
        "WHERE roomId=:roomId group by memberId order by result desc ",
        nativeQuery = true)
     List<Vote> findAllByRoomId(@Param("roomId")Long roomId);
    //Optional<Vote> findByRoundNumAndRoomId(int roundNum, Long roomId);
}
