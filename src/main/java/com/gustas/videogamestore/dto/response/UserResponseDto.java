package com.gustas.videogamestore.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
public class UserResponseDto {

    private String username;
    private String email;
    private List<GameResponseDto> games;

}
