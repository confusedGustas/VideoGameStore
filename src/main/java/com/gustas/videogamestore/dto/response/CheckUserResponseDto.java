package com.gustas.videogamestore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CheckUserResponseDto {

    private Boolean userLoggedIn;
    private Boolean isAdmin;

}
