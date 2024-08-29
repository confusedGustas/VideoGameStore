package com.gustas.videogamestore.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LoginUserRequestDto {

    @NotNull(message = "A Username must be provided")
    String username;

    @NotNull(message = "A Password must be provided")
    String password;

}
