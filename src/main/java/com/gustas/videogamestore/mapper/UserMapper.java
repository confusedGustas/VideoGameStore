package com.gustas.videogamestore.mapper;

import com.gustas.videogamestore.domain.User;
import com.gustas.videogamestore.dto.request.SaveUserRequestDto;
import com.gustas.videogamestore.dto.response.UserDetailsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper {

    public User toEntity(SaveUserRequestDto saveUserRequestDto) {
        User user = new User();

        user.setUsername(saveUserRequestDto.getUsername());
        user.setPassword(saveUserRequestDto.getPassword());
        user.setEmail(saveUserRequestDto.getEmail());

        return user;
    }

    public UserDetailsResponseDto toDto(User user) {
        UserDetailsResponseDto userDetailsResponseDto = new UserDetailsResponseDto();

        userDetailsResponseDto.setId(user.getId());
        userDetailsResponseDto.setUsername(user.getUsername());
        userDetailsResponseDto.setEmail(user.getEmail());
        userDetailsResponseDto.setEnabled(user.isEnabled());
        userDetailsResponseDto.setRole(String.valueOf(user.getRole()));

        return userDetailsResponseDto;
    }

    public List<UserDetailsResponseDto> toDto(List<User> games) {
        if (games == null || games.isEmpty()) {
            return Collections.emptyList();
        }

        return games.stream()
                .map(this::toDto)
                .toList();
    }

}
