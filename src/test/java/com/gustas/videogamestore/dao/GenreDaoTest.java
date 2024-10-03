package com.gustas.videogamestore.dao;

import com.gustas.videogamestore.AbstractIntegrationTest;
import com.gustas.videogamestore.dao.genre.GenreDao;
import com.gustas.videogamestore.domain.Genre;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;

import static com.gustas.videogamestore.constants.TestConstants.INVALID_GENRE_NAME;
import static com.gustas.videogamestore.constants.TestConstants.VALID_GENRE_NAME;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;

public class GenreDaoTest extends AbstractIntegrationTest {

    @Autowired
    private GenreDao genreDao;

    @Test
    public void getValidGenre() {
        Optional<Genre> genre = genreDao.findByGenreName(VALID_GENRE_NAME);

        assertNotNull(genre);
        genre.ifPresent(value -> assertEquals(VALID_GENRE_NAME, value.getName()));
    }

    @Test
    public void getInvalidGenre() {
        Optional<Genre> genre = genreDao.findByGenreName(INVALID_GENRE_NAME);

        assertEquals(Optional.empty(), genre);
    }

}
