package com.gustas.videogamestore.dao.Origin;

import com.gustas.videogamestore.domain.Origin;
import java.util.Optional;

public interface OriginDao {

    Origin save(Origin origin);
    Optional<Origin> findByOriginName(String origin);

}
