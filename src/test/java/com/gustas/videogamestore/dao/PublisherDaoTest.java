package com.gustas.videogamestore.dao;

import com.gustas.videogamestore.AbstractIntegrationTest;
import com.gustas.videogamestore.dao.Publisher.PublisherDao;
import com.gustas.videogamestore.domain.Publisher;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;

import static com.gustas.videogamestore.constants.TestConstants.INVALID_PUBLISHER_NAME;
import static com.gustas.videogamestore.constants.TestConstants.VALID_PUBLISHER_NAME;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;

public class PublisherDaoTest extends AbstractIntegrationTest {

    @Autowired
    private PublisherDao publisherDao;

    @Test
    public void getValidPublisher() {
        Optional<Publisher> publisher = publisherDao.findByPublisherName(VALID_PUBLISHER_NAME);

        assertNotNull(publisher);
        publisher.ifPresent(value -> assertEquals(value.getPublisher(), VALID_PUBLISHER_NAME));
    }

    @Test
    public void getInvalidPublisher() {
        Optional<Publisher> publisher = publisherDao.findByPublisherName(INVALID_PUBLISHER_NAME);

        assertEquals(Optional.empty(), publisher);
    }

}
