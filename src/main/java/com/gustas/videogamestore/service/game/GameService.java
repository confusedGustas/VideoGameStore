package com.gustas.videogamestore.service.game;

import com.gustas.videogamestore.domain.GameSearchCriteria;
import com.gustas.videogamestore.dto.request.GameRequestDto;
import com.gustas.videogamestore.dto.response.GameResponseDto;
import com.gustas.videogamestore.dto.response.PaginatedResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
public interface GameService {

    GameResponseDto saveGame(GameRequestDto gameRequestDto, MultipartFile file) throws IOException;
    PaginatedResponseDto getGames(GameSearchCriteria gameSearchCriteria);
    void deleteGame(Long gameId);
    GameResponseDto getGame(Long gameId);
    void updateGame(Long gameId, GameRequestDto gameRequestDto, MultipartFile file) throws IOException;

}
