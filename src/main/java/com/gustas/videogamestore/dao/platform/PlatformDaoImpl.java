package com.gustas.videogamestore.dao.platform;

import com.gustas.videogamestore.domain.Platform;
import com.gustas.videogamestore.repository.PlatformRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PlatformDaoImpl implements PlatformDao {

    private PlatformRepository platformRepository;

    @Override
    public Platform save(Platform platform) {
        return platformRepository.save(platform);
    }

    @Override
    public Optional<Platform> findByPlatformName(String platformName) {
        return platformRepository.findByPlatformName(platformName);
    }

}
