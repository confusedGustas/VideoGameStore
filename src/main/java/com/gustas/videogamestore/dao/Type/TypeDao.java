package com.gustas.videogamestore.dao.Type;

import com.gustas.videogamestore.domain.Type;
import java.util.Optional;

public interface TypeDao {

    Type save(Type type);
    Optional<Type> findByTypeName(String typeName);

}
