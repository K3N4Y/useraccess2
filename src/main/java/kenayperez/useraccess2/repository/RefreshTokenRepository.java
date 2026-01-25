package kenayperez.useraccess2.repository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshToken {
    Optional<RefreshToken> findByToken(String token);
}
