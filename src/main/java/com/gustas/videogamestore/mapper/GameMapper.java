package com.gustas.videogamestore.mapper;

import com.gustas.videogamestore.dao.rating.RatingDao;
import com.gustas.videogamestore.domain.Game;
import com.gustas.videogamestore.domain.Genre;
import com.gustas.videogamestore.domain.Platform;
import com.gustas.videogamestore.domain.Publisher;
import com.gustas.videogamestore.domain.Region;
import com.gustas.videogamestore.dto.request.GameRequestDto;
import com.gustas.videogamestore.dto.response.GameResponseDto;
import com.gustas.videogamestore.service.session.SessionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;

@Component
@AllArgsConstructor
public class GameMapper {

    private RatingDao ratingDao;
    private SessionService sessionService;

    public Game toEntity(GameRequestDto gameRequestDto, Genre genre, Platform platform, Region region, Publisher publisher) {
        Game game = new Game();

        game.setName(gameRequestDto.getName());
        game.setPrice(gameRequestDto.getPrice());
        game.setReleaseYear(gameRequestDto.getReleaseYear());
        game.setGenre(genre);
        game.setActivationPlatform(platform);
        game.setStock(gameRequestDto.getStock());
        game.setDescription(gameRequestDto.getDescription());
        game.setActivationRegion(region);
        game.setPublisher(publisher);

        return game;
    }

    public GameResponseDto toDto(Game game) {
        GameResponseDto dto = new GameResponseDto();

        dto.setId(game.getId());
        dto.setName(game.getName());
        dto.setPrice(game.getPrice());
        dto.setReleaseYear(game.getReleaseYear());
        dto.setGenre(game.getGenre());
        dto.setActivationPlatform(game.getActivationPlatform());
        dto.setStock(game.getStock());
        dto.setDescription(game.getDescription());
        dto.setActivationRegion(game.getActivationRegion());
        dto.setPublisher(game.getPublisher());
        dto.setSeller(game.getUser().getUsername());

        Double averageRating = ratingDao.findAverageRatingByGameId(game.getId());
        dto.setAverageRating(averageRating != null ? (int) Math.round(averageRating) : 0);

        return dto;
    }

    public List<GameResponseDto> toDto(List<Game> games) {
        if (games == null || games.isEmpty()) {
            return Collections.emptyList();
        }

        return games.stream()
                .map(this::toDto)
                .toList();
    }

}
