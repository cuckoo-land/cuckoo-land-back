package com.example.cuckoolandback.majority.repository;

import com.example.cuckoolandback.majority.domain.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface PictureRepository extends JpaRepository<Picture,Long> {

    @Query(value = "SELECT * FROM Picture\n" +
            "WHERE majorityId = :majorityId\n"+
            "ORDER BY RAND()",
            nativeQuery = true)
    List<Picture> findPicturesByRandom(@Param("majorityId") Long majorityId);
    Picture findPictureById(Long id);
    Picture findPictureByName(String name);
    List<Picture> findFirstByMajorityIdOrderByNumOfWinsDesc();
    @Query(value = "SELECT distinct majorityId FROM Picture\n",
        nativeQuery = true)
    int countAllByMajorityId();

    @Query(value = "SELECT * FROM Picture\n" +
        "WHERE majorityId = :majorityId\n"+
        "ORDER BY numOfWins desc limit 3",
        nativeQuery = true)
    List<Picture> findPictureByMajorityId(@Param("majorityId")Long majorityId);


}
