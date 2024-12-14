package com.gustas.videogamestore.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserRequestDto {

    @NotNull(message = "A Username must be provided")
    private String username;

    @NotNull(message = "A Password must be provided")
    private String password;

}
