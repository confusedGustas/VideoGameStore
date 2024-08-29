package com.gustas.videogamestore.dto.response;

import lombok.Setter;
import java.util.List;

@Setter
public class UserResponseDto {

    private String username;
    private String email;
    private List<GameResponseDto> games;

}
