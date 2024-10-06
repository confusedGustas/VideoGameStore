package com.gustas.videogamestore.dao;

import com.gustas.videogamestore.AbstractIntegrationTest;
import com.gustas.videogamestore.TestConfiguration;
import com.gustas.videogamestore.dao.genre.GenreDao;
import com.gustas.videogamestore.domain.Genre;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static com.gustas.videogamestore.constants.TestConstants.INVALID_GENRE_NAME;
import static com.gustas.videogamestore.constants.TestConstants.VALID_GENRE_NAME;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@ContextConfiguration(classes = {TestConfiguration.class})
class GenreDaoTest extends AbstractIntegrationTest {

    @Autowired
    private GenreDao genreDao;

    @Test
    void getValidGenre() {
        Optional<Genre> genre = genreDao.findByGenreName(VALID_GENRE_NAME);

        assertNotNull(genre);
        genre.ifPresent(value -> Assertions.assertEquals(VALID_GENRE_NAME, value.getName()));
    }

    @Test
    void getInvalidGenre() {
        Optional<Genre> genre = genreDao.findByGenreName(INVALID_GENRE_NAME);

        Assertions.assertEquals(Optional.empty(), genre);
    }

}
