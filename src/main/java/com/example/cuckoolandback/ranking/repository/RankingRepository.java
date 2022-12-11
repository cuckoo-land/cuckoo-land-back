package com.example.cuckoolandback.ranking.repository;

import com.example.cuckoolandback.friend.domain.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankingRepository extends JpaRepository<Friend, Long> {

}
