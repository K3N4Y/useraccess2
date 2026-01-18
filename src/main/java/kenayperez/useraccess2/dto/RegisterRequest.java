package kenayperez.useraccess2.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    private String username;

    @Email
    private String email;

    @Size(min = 6, message = "password must be at least 6 characters")
    private String password;
}
