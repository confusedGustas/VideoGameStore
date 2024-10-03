package com.gustas.videogamestore.controller;

import com.gustas.videogamestore.domain.GameSearchCriteria;
import com.gustas.videogamestore.domain.SortOrder;
import com.gustas.videogamestore.dto.request.SaveGameRequestDto;
import com.gustas.videogamestore.dto.response.GameResponseDto;
import com.gustas.videogamestore.dto.response.PaginatedResponseDto;
import com.gustas.videogamestore.service.game.GameService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/games")
@AllArgsConstructor
@Tag(name = "Game Controllers")
public class GameController {

    private GameService gameService;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.OK)
    @CacheEvict(value = "games", allEntries = true)
    public GameResponseDto saveGame(@Valid @RequestBody SaveGameRequestDto saveGameRequestDto) {
        return gameService.saveGame(saveGameRequestDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Cacheable(value = "games", key = "{#limit, #offset, #sortOrder, #sortColumn, #search}")
    public PaginatedResponseDto getGames(@RequestParam(required = false) Integer limit,
                                         @RequestParam(required = false) Integer offset,
                                         @RequestParam(required = false) SortOrder sortOrder,
                                         @RequestParam(required = false) String sortColumn,
                                         @RequestParam(required = false) String search) {
        return gameService.getGames(new GameSearchCriteria(limit, offset, sortOrder, sortColumn, search));
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    @CacheEvict(value = "games", allEntries = true)
    public void deleteGame(@RequestParam Long gameId) {
        gameService.deleteGame(gameId);
    }

    @GetMapping("get/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    public GameResponseDto getGame(@PathVariable Long gameId) {
        return gameService.getGame(gameId);
    }

}
