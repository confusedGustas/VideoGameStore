package com.gustas.videogamestore.service;

import com.gustas.videogamestore.AbstractIntegrationTest;
import com.gustas.videogamestore.dto.request.SaveGameRequestDto;
import com.gustas.videogamestore.dto.response.GameResponseDto;
import com.gustas.videogamestore.service.game.GameService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertThrows;

public class GameServiceTest extends AbstractIntegrationTest {

    private GameResponseDto testGame;

    @Autowired
    private GameService gameService;

    @Test
    public void testSaveNormalGame() {
        SaveGameRequestDto saveGameRequestDto = new SaveGameRequestDto(
                "Test Game",
                BigDecimal.valueOf(14.99),
                "Test Description",
                "Test.jpg",
                2001,
                "Test Platform",
                "Test Publisher",
                "Test Developer",
                "Test Release Date",
                BigDecimal.valueOf(100)
        );

        testGame = gameService.saveGame(saveGameRequestDto);

        assertNotNull(testGame);
        Assertions.assertEquals(saveGameRequestDto.getName(), testGame.getName());
        Assertions.assertEquals(saveGameRequestDto.getPrice(), testGame.getPrice());
        Assertions.assertEquals(saveGameRequestDto.getDescription(), testGame.getDescription());
        Assertions.assertEquals(saveGameRequestDto.getImage(), testGame.getImage());
        Assertions.assertEquals(saveGameRequestDto.getReleaseYear(), testGame.getReleaseYear());
        Assertions.assertEquals(saveGameRequestDto.getPlatform(), testGame.getActivationPlatform().getPlatformName());
        Assertions.assertEquals(saveGameRequestDto.getPublisher(), testGame.getPublisher().getPublisherName());
        Assertions.assertEquals(saveGameRequestDto.getRegion(), testGame.getActivationRegion().getRegionName());
        Assertions.assertEquals(saveGameRequestDto.getStock(), testGame.getStock());
    }

    @Test
    public void testSaveNullGames() {
        SaveGameRequestDto saveGameRequestDto = new SaveGameRequestDto(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertThrows(DataIntegrityViolationException.class, () -> testGame = gameService.saveGame(saveGameRequestDto) );
        Assertions.assertNull(testGame);
    }

}
