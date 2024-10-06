package com.gustas.videogamestore.dao;

import com.gustas.videogamestore.AbstractIntegrationTest;
import com.gustas.videogamestore.TestConfiguration;
import com.gustas.videogamestore.dao.platform.PlatformDao;
import com.gustas.videogamestore.domain.Platform;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static com.gustas.videogamestore.constants.TestConstants.INVALID_PLATFORM_NAME;
import static com.gustas.videogamestore.constants.TestConstants.VALID_PLATFORM_NAME;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@ContextConfiguration(classes = {TestConfiguration.class})
class PlatformDaoTest extends AbstractIntegrationTest {

    @Autowired
    private PlatformDao platformDao;

    @Test
    void getValidPlatform() {
        Optional<Platform> platform = platformDao.findByPlatformName(VALID_PLATFORM_NAME);

        assertNotNull(platform);
        platform.ifPresent(value -> Assertions.assertEquals(VALID_PLATFORM_NAME, value.getPlatformName()));
    }

    @Test
    void getInvalidPlatform() {
        Optional<Platform> platform = platformDao.findByPlatformName(INVALID_PLATFORM_NAME);

        Assertions.assertEquals(Optional.empty(), platform);
    }

}
