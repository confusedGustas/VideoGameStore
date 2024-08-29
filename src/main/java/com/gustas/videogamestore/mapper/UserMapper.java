package com.gustas.videogamestore.mapper;

import com.gustas.videogamestore.domain.User;
import com.gustas.videogamestore.dto.request.SaveUserRequestDto;
import com.gustas.videogamestore.dto.response.UserResponseDto;

public class UserMapper {

    public static User toEntity(SaveUserRequestDto saveUserRequestDto) {
        User user = new User();

        user.setUsername(saveUserRequestDto.getUsername());
        user.setPassword(saveUserRequestDto.getPassword());
        user.setEmail(saveUserRequestDto.getEmail());

        return user;
    }

    public static UserResponseDto toDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();

        userResponseDto.setUsername(user.getUsername());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setGames(GameMapper.toDto(user.getGames()));

        return userResponseDto;
    }

}
