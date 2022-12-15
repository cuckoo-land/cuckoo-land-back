package com.example.cuckoolandback.majority.repository;

import com.example.cuckoolandback.majority.domain.Vs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VsRepository extends JpaRepository<Vs,Long> {
}
