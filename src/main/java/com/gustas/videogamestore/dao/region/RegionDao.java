package com.gustas.videogamestore.dao.region;

import com.gustas.videogamestore.domain.Region;
import java.util.Optional;

public interface RegionDao {

    Region save(Region region);
    Optional<Region> findByRegionName(String regionName);

}
