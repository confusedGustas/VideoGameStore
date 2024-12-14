package com.gustas.videogamestore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaveUserRequestDto {

    @NotBlank(message = "First name needs to be provided")
    @Pattern(regexp = "^[^A-Za-z]*[A-Za-z].*$", message = "First name must contain at least one English letter")
    @Schema(example = "Gustas")
    private String username;

    @NotBlank(message = "Password needs to be provided")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[&#!%])(?=.*\\d).*$", message = "Password must contain a special character and a digit")
    @Schema(example = "Gustas123!")
    private String password;

    @NotBlank(message = "Email needs to be provided")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Invalid email format")
    @Schema(example = "Gustas@gmail.com")
    private String email;

}
