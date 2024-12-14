package com.gustas.videogamestore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeUserUsernameDto {

    @NotBlank(message = "New Username needs to be provided")
    @Pattern(regexp = "^[^A-Za-z]*[A-Za-z].*$", message = "New Username must contain at least one English letter")
    @Schema(example = "Antanas321")
    private String username;

}
