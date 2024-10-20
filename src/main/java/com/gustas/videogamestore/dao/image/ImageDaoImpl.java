package com.gustas.videogamestore.dao.image;

import com.gustas.videogamestore.domain.Image;
import com.gustas.videogamestore.repository.ImageRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ImageDaoImpl implements ImageDao {

    private ImageRepository imageRepository;

    @Override
    @Transactional
    public Optional<Image> getImage(Long gameId) {
        return imageRepository.findById(gameId);
    }

    @Override
    public void save(Image image) {
        imageRepository.save(image);
    }

}
