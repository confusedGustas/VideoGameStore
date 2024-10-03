package com.gustas.videogamestore.dao;

import com.gustas.videogamestore.AbstractIntegrationTest;
import com.gustas.videogamestore.dao.region.RegionDao;
import com.gustas.videogamestore.domain.Region;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;

import static com.gustas.videogamestore.constants.TestConstants.INVALID_REGION_NAME;
import static com.gustas.videogamestore.constants.TestConstants.VALID_REGION_NAME;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;

public class RegionDaoTest extends AbstractIntegrationTest {

    @Autowired
    private RegionDao regionDao;

    @Test
    public void getValidRegion() {
        Optional<Region> region = regionDao.findByRegionName(VALID_REGION_NAME);

        assertNotNull(region);
        region.ifPresent(value -> assertEquals(VALID_REGION_NAME, value.getRegionName()));
    }

    @Test
    public void getInvalidRegion() {
        Optional<Region> region = regionDao.findByRegionName(INVALID_REGION_NAME);

        assertEquals(Optional.empty(), region);
    }

}
