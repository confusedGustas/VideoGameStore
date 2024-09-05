package com.gustas.videogamestore.mapper;

import com.gustas.videogamestore.domain.Game;
import com.gustas.videogamestore.domain.Genre;
import com.gustas.videogamestore.domain.Platform;
import com.gustas.videogamestore.domain.Publisher;
import com.gustas.videogamestore.domain.Region;
import com.gustas.videogamestore.dto.request.SaveGameRequestDto;
import com.gustas.videogamestore.dto.response.GameResponseDto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GameMapper {

    public static Game toEntity(SaveGameRequestDto saveGameRequestDto,
                                 Genre genre,
                                 Platform platform,
                                 Region region,
                                 Publisher publisher) {
        Game game = new Game();

        game.setName(saveGameRequestDto.getName());
        game.setPrice(saveGameRequestDto.getPrice());
        game.setReleaseYear(saveGameRequestDto.getReleaseYear());
        game.setGenre(genre);
        game.setActivationPlatform(platform);
        game.setStock(saveGameRequestDto.getStock());
        game.setImage(saveGameRequestDto.getImage());
        game.setDescription(saveGameRequestDto.getDescription());
        game.setActivationRegion(region);
        game.setPublisher(publisher);

        return game;
    }

    public static GameResponseDto toDto(Game game) {
        GameResponseDto dto = new GameResponseDto();

        dto.setId(game.getId());
        dto.setName(game.getName());
        dto.setPrice(game.getPrice());
        dto.setReleaseYear(game.getReleaseYear());
        dto.setGenre(game.getGenre());
        dto.setActivationPlatform(game.getActivationPlatform());
        dto.setStock(game.getStock());
        dto.setImage(game.getImage());
        dto.setDescription(game.getDescription());
        dto.setActivationRegion(game.getActivationRegion());
        dto.setPublisher(game.getPublisher());
        dto.setSeller(game.getUser().getUsername());

        return dto;
    }

    public static List<GameResponseDto> toDto(List<Game> games) {
        if (games == null) return null;
        if (games.isEmpty()) return Collections.emptyList();

        return games.stream().map(GameMapper::toDto).collect(Collectors.toList());
    }


}
