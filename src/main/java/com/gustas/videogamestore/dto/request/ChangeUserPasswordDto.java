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
@NoArgsConstructor
@AllArgsConstructor
public class ChangeUserPasswordDto {

    @NotBlank(message = "Password needs to be provided")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[&#!%])(?=.*\\d).*$", message = "Password must contain a special character and a digit")
    @Schema(example = "Gustas123!")
    private String password;

}
