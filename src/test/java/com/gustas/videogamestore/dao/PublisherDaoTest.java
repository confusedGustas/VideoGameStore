package com.gustas.videogamestore.dao;

import com.gustas.videogamestore.AbstractIntegrationTest;
import com.gustas.videogamestore.dao.publisher.PublisherDao;
import com.gustas.videogamestore.domain.Publisher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.gustas.videogamestore.constants.TestConstants.INVALID_PUBLISHER_NAME;
import static com.gustas.videogamestore.constants.TestConstants.VALID_PUBLISHER_NAME;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

class PublisherDaoTest extends AbstractIntegrationTest {

    @Autowired
    private PublisherDao publisherDao;

    @Test
    void getValidPublisher() {
        Optional<Publisher> publisher = publisherDao.findByPublisherName(VALID_PUBLISHER_NAME);

        assertNotNull(publisher);
        publisher.ifPresent(value -> Assertions.assertEquals(VALID_PUBLISHER_NAME, value.getPublisherName()));
    }

    @Test
    void getInvalidPublisher() {
        Optional<Publisher> publisher = publisherDao.findByPublisherName(INVALID_PUBLISHER_NAME);

        Assertions.assertEquals(Optional.empty(), publisher);
    }

}
