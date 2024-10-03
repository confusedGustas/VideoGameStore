package com.gustas.videogamestore.dao;

import com.gustas.videogamestore.AbstractIntegrationTest;
import com.gustas.videogamestore.dao.platform.PlatformDao;
import com.gustas.videogamestore.domain.Platform;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;

import static com.gustas.videogamestore.constants.TestConstants.INVALID_PLATFORM_NAME;
import static com.gustas.videogamestore.constants.TestConstants.VALID_PLATFORM_NAME;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;

public class PlatformDaoTest extends AbstractIntegrationTest {

    @Autowired
    private PlatformDao platformDao;

    @Test
    public void getValidPlatform() {
        Optional<Platform> platform = platformDao.findByPlatformName(VALID_PLATFORM_NAME);

        assertNotNull(platform);
        platform.ifPresent(value -> assertEquals(VALID_PLATFORM_NAME, value.getPlatformName()));
    }

    @Test
    public void getInvalidPlatform() {
        Optional<Platform> platform = platformDao.findByPlatformName(INVALID_PLATFORM_NAME);

        assertEquals(Optional.empty(), platform);
    }

}
