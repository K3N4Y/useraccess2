package kenayperez.useraccess2.repository;

import kenayperez.useraccess2.dto.enums.TokenStatus;
import kenayperez.useraccess2.entities.RefreshToken;
import kenayperez.useraccess2.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenHash(String tokenHash);

    List<RefreshToken> findByUserAndStatus(UserEntity user, TokenStatus status);

    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.status = :status WHERE rt.user = :user AND rt.status = :oldStatus")
    void updateStatusByUserAndStatus(UserEntity user, TokenStatus oldStatus, TokenStatus status);

    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiresAt < :now AND rt.status = :status")
    void deleteExpiredTokens(Instant now, TokenStatus status);
}
