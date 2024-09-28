package com.gustas.videogamestore.util;

import com.gustas.videogamestore.domain.Image;
import com.gustas.videogamestore.repository.ImageRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
@AllArgsConstructor
public class ImageUtil {

    private ImageRepository imageRepository;

    public static byte[] compressImage(byte[] data) throws IOException {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];
        while (!deflater.finished()) {
            int size = deflater.deflate(tmp);
            outputStream.write(tmp, 0, size);
        }

        outputStream.close();

        return outputStream.toByteArray();
    }

    public static byte[] decompressImage(byte[] data) throws IOException, DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);

        byte[] tmp = new byte[4*1024];

        while (!inflater.finished()) {
            int count = inflater.inflate(tmp);
            outputStream.write(tmp, 0, count);
        }
        outputStream.close();

        return outputStream.toByteArray();
    }

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
                    imageEntity.setImage(compressImage(imageBytes));
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
