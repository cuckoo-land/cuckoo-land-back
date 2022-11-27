package com.example.cuckoolandback.user.repository;

import com.example.cuckoolandback.user.jwt.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    void deleteByToken(String refreshToken);
}
