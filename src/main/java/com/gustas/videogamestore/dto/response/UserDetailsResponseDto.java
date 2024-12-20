package com.gustas.videogamestore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsResponseDto {

    private Long id;
    private String username;
    private String email;
    private Boolean enabled;
    private String role;

}
