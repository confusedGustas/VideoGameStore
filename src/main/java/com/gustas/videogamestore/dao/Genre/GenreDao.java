package com.gustas.videogamestore.dao.Genre;

import com.gustas.videogamestore.domain.Genre;
import java.util.Optional;

public interface GenreDao {

    Genre save(Genre genre);
    Optional<Genre> findByGenreName(String genre);

}
