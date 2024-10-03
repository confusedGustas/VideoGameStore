package com.gustas.videogamestore.service;

import com.gustas.videogamestore.AbstractIntegrationTest;
import com.gustas.videogamestore.dto.request.SaveGameRequestDto;
import com.gustas.videogamestore.dto.response.GameResponseDto;
import com.gustas.videogamestore.service.game.GameService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
        assertEquals(saveGameRequestDto.getName(), testGame.getName());
        assertEquals(saveGameRequestDto.getPrice(), testGame.getPrice());
        assertEquals(saveGameRequestDto.getDescription(), testGame.getDescription());
        assertEquals(saveGameRequestDto.getImage(), testGame.getImage());
        assertEquals(saveGameRequestDto.getReleaseYear(), testGame.getReleaseYear());
        assertEquals(saveGameRequestDto.getPlatform(), testGame.getActivationPlatform().getPlatformName());
        assertEquals(saveGameRequestDto.getPublisher(), testGame.getPublisher().getPublisherName());
        assertEquals(saveGameRequestDto.getRegion(), testGame.getActivationRegion().getRegionName());
        assertEquals(saveGameRequestDto.getStock(), testGame.getStock());
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
        assertNull(testGame);
    }

}
