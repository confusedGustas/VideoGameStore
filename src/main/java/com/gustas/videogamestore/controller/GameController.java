package com.gustas.videogamestore.controller;

import com.gustas.videogamestore.domain.GameSearchCriteria;
import com.gustas.videogamestore.domain.SortOrder;
import com.gustas.videogamestore.dto.request.SaveGameRequestDto;
import com.gustas.videogamestore.dto.response.PaginatedResponseDto;
import com.gustas.videogamestore.service.Game.GameService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/games")
@AllArgsConstructor
@Tag(name = "Game Controllers")
public class GameController {

    private GameService gameService;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.OK)
    @CacheEvict(value = "games", allEntries = true)
    public void saveGame(@Valid @RequestBody SaveGameRequestDto saveGameRequestDto) {
        gameService.saveGame(saveGameRequestDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Cacheable(value = "games", key = "{#limit, #offset, #sortOrder, #sortColumn}")
    public PaginatedResponseDto getGames(@RequestParam(required = false) Integer limit,
                                         @RequestParam(required = false) Integer offset,
                                         @RequestParam(required = false) SortOrder sortOrder,
                                         @RequestParam(required = false) String sortColumn) {
        return gameService.getGames(new GameSearchCriteria(limit, offset, sortOrder, sortColumn));
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    @CacheEvict(value = "games", allEntries = true)
    public void deleteGame(@RequestParam Long gameId) {
        gameService.deleteGame(gameId);
    }

}
