package com.example.cuckoolandback.majority.repository;

import com.example.cuckoolandback.majority.domain.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface PictureRepository extends JpaRepository<Picture,Long> {

    @Query(value = "SELECT * FROM picture\n" +
            "WHERE majority_id = :majorityId\n"+
            "ORDER BY RAND()",
            nativeQuery = true)
    List<Picture> findPicturesByRandom(@Param("majorityId") Long majorityId);
}
