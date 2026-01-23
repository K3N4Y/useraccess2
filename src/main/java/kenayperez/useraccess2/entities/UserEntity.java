package kenayperez.useraccess2.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kenayperez.useraccess2.dto.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "_users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @ColumnDefault("gen_random_uuid()")
    @Column(name = "id", nullable = false)
    private UUID id;

    @Size(max = 255)
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull
    @Column(name = "password_hash", nullable = false, length = Integer.MAX_VALUE)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 10)
    private UserStatus status;

    @CreationTimestamp
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "username", nullable = false, length = 20, unique = true)
    private String username;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles;
}
