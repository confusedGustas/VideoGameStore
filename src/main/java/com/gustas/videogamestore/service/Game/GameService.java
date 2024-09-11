package com.gustas.videogamestore.service.Game;

import com.gustas.videogamestore.domain.GameSearchCriteria;
import com.gustas.videogamestore.dto.request.SaveGameRequestDto;
import com.gustas.videogamestore.dto.response.GameResponseDto;
import com.gustas.videogamestore.dto.response.PaginatedResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface GameService {

    GameResponseDto saveGame(SaveGameRequestDto saveGameRequestDto);
    PaginatedResponseDto getGames(GameSearchCriteria gameSearchCriteria);
    void deleteGame(Long gameId);

}
