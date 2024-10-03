package com.gustas.videogamestore.dao.platform;

import com.gustas.videogamestore.domain.Platform;
import java.util.Optional;

public interface PlatformDao {

    Platform save(Platform platform);
    Optional<Platform> findByPlatformName(String platformName);

}
