package com.jasoseohelper.user.repository;

import com.jasoseohelper.user.entity.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByEmail(String email);
    boolean existsByToken(String refresh);
    int deleteByEmail(String email);
}
