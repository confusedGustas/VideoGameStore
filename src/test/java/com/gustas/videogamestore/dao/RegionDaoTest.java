package com.gustas.videogamestore.dao;

import com.gustas.videogamestore.AbstractIntegrationTest;
import com.gustas.videogamestore.dao.region.RegionDao;
import com.gustas.videogamestore.domain.Region;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.gustas.videogamestore.constants.TestConstants.INVALID_REGION_NAME;
import static com.gustas.videogamestore.constants.TestConstants.VALID_REGION_NAME;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

class RegionDaoTest extends AbstractIntegrationTest {

    @Autowired
    private RegionDao regionDao;

    @Test
    void getValidRegion() {
        Optional<Region> region = regionDao.findByRegionName(VALID_REGION_NAME);

        assertNotNull(region);
        region.ifPresent(value -> Assertions.assertEquals(VALID_REGION_NAME, value.getRegionName()));
    }

    @Test
    void getInvalidRegion() {
        Optional<Region> region = regionDao.findByRegionName(INVALID_REGION_NAME);

        Assertions.assertEquals(Optional.empty(), region);
    }

}
