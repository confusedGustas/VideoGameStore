package com.gustas.videogamestore.dao.Type;

import com.gustas.videogamestore.domain.Type;
import com.gustas.videogamestore.repository.TypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TypeDaoImpl implements TypeDao {

    private TypeRepository typeRepository;

    @Override
    public Type save(Type type) {
        return typeRepository.save(type);
    }

    @Override
    public Optional<Type> findByTypeName(String typeName) {
        return typeRepository.findByType(typeName);
    }

}
