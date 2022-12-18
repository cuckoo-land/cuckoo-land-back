package com.example.cuckoolandback.room.repository;

import com.example.cuckoolandback.room.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant,String> {

    List<Participant> findByRoomId(Long roomid);
    @Query(value = "SELECT COUNT(*) FROM participant\n" +
            "WHERE roomId = :roomId",
            nativeQuery = true)
    int numOfParticipants(@Param("roomId")Long roomId);

    Participant findFirstByRoomId(Long roomId);
}
