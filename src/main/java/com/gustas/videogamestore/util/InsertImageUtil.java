package com.gustas.videogamestore.util;

import com.gustas.videogamestore.domain.Image;
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

    private ImageRepository imageRepository;

    @PostConstruct
    public void uploadImagesFromStaticFolder() {
        if (imageRepository.count() > 0) return;

        Path staticFolderPath = Paths.get("src/main/resources/static/images");

        try (Stream<Path> fileStream = Files.list(staticFolderPath)) {
            fileStream.forEach(filePath -> {
                byte[] imageBytes;
                Image imageEntity = new Image();

                try {
                    imageBytes = Files.readAllBytes(filePath);

                    imageEntity.setName(filePath.getFileName().toString());
                    imageEntity.setImage(ImageUtil.compressImage(imageBytes));
                } catch (IOException e) {
                    throw new RuntimeException("Error reading image bytes: " + filePath, e);
                }

                imageRepository.save(imageEntity);
            });
        } catch (IOException e) {
            throw new RuntimeException("Error listing files in static folder: " + staticFolderPath, e);
        }
    }

}
