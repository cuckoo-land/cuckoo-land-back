package com.example.cuckoolandback.room.repository;

import com.example.cuckoolandback.room.domain.Room;
import com.example.cuckoolandback.room.dto.RoomResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room,Long> {

    Optional<Room> findRoomByCode(String code);
    Optional<Room> findById(Long id);

    @Query(value = "SELECT * FROM room\n" +
            "WHERE MATCH (host_id, title) AGAINST (:keyword IN BOOLEAN MODE)",
            countQuery = "SELECT count(*) FROM room WHERE MATCH (host_id, title) AGAINST (:keyword IN BOOLEAN MODE)",
            nativeQuery = true)
    List<Room> searchRooms(@Param("keyword") String keyword, @PageableDefault Pageable pageable);

}
