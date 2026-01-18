package kenayperez.useraccess2.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserDto(UUID id, String name, String email, OffsetDateTime createdAt) {
}
