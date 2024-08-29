package com.gustas.videogamestore.dao.Genre;

import com.gustas.videogamestore.domain.Genre;
import com.gustas.videogamestore.repository.GenreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GenreDaoImpl implements GenreDao {

    private GenreRepository genreRepository;

    @Override
    public Genre save(Genre genre) {
        return genreRepository.save(genre);
    }

    @Override
    public Optional<Genre> findByGenreName(String genre) {
        return genreRepository.findByGenre(genre);
    }

}
