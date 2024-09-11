package com.gustas.videogamestore.dao.Game;

import com.gustas.videogamestore.domain.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GameDao {

    Game saveGame(Game game);
    Page<Game> findAll(Pageable pageable);
    void deleteGame(Long gameId);

}
