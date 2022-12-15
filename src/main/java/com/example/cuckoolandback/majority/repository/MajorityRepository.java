package com.example.cuckoolandback.majority.repository;

import com.example.cuckoolandback.majority.domain.Majority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MajorityRepository extends JpaRepository<Majority, Long> {
}
