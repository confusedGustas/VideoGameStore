package com.gustas.videogamestore.dao.Origin;

import com.gustas.videogamestore.domain.Origin;
import com.gustas.videogamestore.repository.OriginRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OriginDaoImpl implements OriginDao {

    private OriginRepository originRepository;

    @Override
    public Origin save(Origin origin) {
        return originRepository.save(origin);
    }

    @Override
    public Optional<Origin> findByOriginName(String origin) {
        return originRepository.findByOrigin(origin);
    }

}
