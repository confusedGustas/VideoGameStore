package com.gustas.videogamestore.dao.Region;

import com.gustas.videogamestore.domain.Region;
import com.gustas.videogamestore.repository.RegionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RegionDaoImpl implements RegionDao {

    private RegionRepository regionRepository;

    @Override
    public Region save(Region region) {
        return regionRepository.save(region);
    }

    @Override
    public Optional<Region> findByRegionName(String regionName) {
        return regionRepository.findByRegion(regionName);
    }

}
