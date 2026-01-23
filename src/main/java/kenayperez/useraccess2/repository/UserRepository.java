package kenayperez.useraccess2.repository;

import kenayperez.useraccess2.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, java.util.UUID> {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
