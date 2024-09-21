package com.gustas.videogamestore.dao.Game;

import com.gustas.videogamestore.domain.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface GameDao {

    Game saveGame(Game game);
    Page<Game> findAll(Specification<Game> specification, Pageable pageable);
    void deleteGame(Long gameId);

}
