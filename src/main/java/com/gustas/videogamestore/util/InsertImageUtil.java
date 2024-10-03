package com.gustas.videogamestore.util;

import com.gustas.videogamestore.domain.Image;
import com.gustas.videogamestore.exception.ImageProcessingException;
import com.gustas.videogamestore.repository.ImageRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
public class InsertImageUtil {

    private final ImageRepository imageRepository;

    @PostConstruct
    public void uploadImagesFromStaticFolder() {
        if (imageRepository.count() > 0) return;

        Path staticFolderPath = Paths.get("src/main/resources/static/images");

        try (Stream<Path> fileStream = Files.list(staticFolderPath)) {
            fileStream.forEach(filePath -> {
                try {
                    byte[] imageBytes = Files.readAllBytes(filePath);
                    Image imageEntity = new Image();

                    imageEntity.setName(filePath.getFileName().toString());
                    imageEntity.setImage(ImageUtil.compressImage(imageBytes));

                    imageRepository.save(imageEntity);
                } catch (IOException e) {
                    throw new ImageProcessingException("Error reading image bytes: " + filePath, e);
                }
            });
        } catch (IOException e) {
            throw new ImageProcessingException("Error listing files in static folder: " + staticFolderPath, e);
        }
    }
}
