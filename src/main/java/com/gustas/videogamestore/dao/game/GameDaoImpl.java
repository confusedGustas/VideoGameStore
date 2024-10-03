package com.gustas.videogamestore.dao.game;

import com.gustas.videogamestore.domain.Game;
import com.gustas.videogamestore.repository.GameRepository;
import com.gustas.videogamestore.service.session.SessionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class GameDaoImpl implements GameDao {

    private GameRepository gameRepository;
    private SessionService sessionService;

    @Override
    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }

    @Override
    public Page<Game> findAll(Specification<Game> specification, Pageable pageable) {
        return gameRepository.findAll(specification, pageable);
    }

    @Override
    public void deleteGame(Long gameId) {
        checkIfGameExists(gameId);
        checkIfUserIsOwner(gameId);

        gameRepository.deleteById(gameId);
    }

    @Override
    public Game getGame(Long gameId) {
        return gameRepository.findById(gameId).orElseThrow(() ->
            new IllegalArgumentException("Game with the specified id does not exist"));
    }

    private void checkIfUserIsOwner(Long gameId) {
        if (sessionService.getUserFromSessionId().getGames().stream().noneMatch(game -> game.getId().equals(gameId))) {
            throw new IllegalArgumentException("User is not the owner of the game");
        }
    }

    private void checkIfGameExists(Long id) {
        if (!gameRepository.existsById(id)) {
            throw new IllegalArgumentException("Game with the specified id does not exist");
        }
    }

}
