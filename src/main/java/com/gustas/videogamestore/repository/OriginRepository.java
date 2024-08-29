package com.gustas.videogamestore.repository;

import com.gustas.videogamestore.domain.Origin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OriginRepository extends JpaRepository<Origin, Long> {

    Optional<Origin> findByOrigin(String origin);

}
