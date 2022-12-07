package com.example.cuckoolandback.room.repository;

import com.example.cuckoolandback.room.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ParticipantRepository extends JpaRepository<Participant,Long> {


}
