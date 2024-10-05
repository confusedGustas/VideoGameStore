package com.gustas.videogamestore.mapper;

import com.gustas.videogamestore.domain.User;
import com.gustas.videogamestore.dto.request.SaveUserRequestDto;
import com.gustas.videogamestore.dto.response.UserDetailsResponseDto;

public class UserMapper {

    private UserMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
    }

    public static User toEntity(SaveUserRequestDto saveUserRequestDto) {
        User user = new User();

        user.setUsername(saveUserRequestDto.getUsername());
        user.setPassword(saveUserRequestDto.getPassword());
        user.setEmail(saveUserRequestDto.getEmail());

        return user;
    }

    public static UserDetailsResponseDto toDto(User user) {
        UserDetailsResponseDto userDetailsResponseDto = new UserDetailsResponseDto();

        userDetailsResponseDto.setUsername(user.getUsername());
        userDetailsResponseDto.setEmail(user.getEmail());

        return userDetailsResponseDto;
    }

}
