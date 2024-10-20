package com.gustas.videogamestore.controller;

import com.gustas.videogamestore.domain.GameSearchCriteria;
import com.gustas.videogamestore.domain.SortOrder;
import com.gustas.videogamestore.dto.request.GameRequestDto;
import com.gustas.videogamestore.dto.response.GameResponseDto;
import com.gustas.videogamestore.dto.response.PaginatedResponseDto;
import com.gustas.videogamestore.service.game.GameService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("api/games")
@AllArgsConstructor
@Tag(name = "Game Controllers")
public class GameController {

    private GameService gameService;

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public GameResponseDto saveGame(@ModelAttribute @Valid GameRequestDto gameRequestDto,
                                    @RequestParam("image") MultipartFile file) throws IOException {
        return gameService.saveGame(gameRequestDto, file);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PaginatedResponseDto getGames(@RequestParam(required = false) Integer limit,
                                         @RequestParam(required = false) Integer offset,
                                         @RequestParam(required = false) SortOrder sortOrder,
                                         @RequestParam(required = false) String sortColumn,
                                         @RequestParam(required = false) String search) {
        return gameService.getGames(new GameSearchCriteria(limit, offset, sortOrder, sortColumn, search));
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteGame(@RequestParam Long gameId) {
        gameService.deleteGame(gameId);
    }

    @GetMapping("get/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    public GameResponseDto getGame(@PathVariable Long gameId) {
        return gameService.getGame(gameId);
    }

    @PostMapping(value = "/update-game", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void updateGame(@ModelAttribute @Valid GameRequestDto gameRequestDto, Long gameId,
                                      @RequestParam("image") MultipartFile file) throws IOException {
        gameService.updateGame(gameId, gameRequestDto, file);
    }

}
