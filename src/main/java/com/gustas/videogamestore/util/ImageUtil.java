package com.gustas.videogamestore.util;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.security.SecureRandom;

@Component
@AllArgsConstructor
public class ImageUtil {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String getRandomFilenameWithExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            int dotIndex = originalFilename.lastIndexOf('.');
            String extension = originalFilename.substring(dotIndex);

            String randomString = generateRandomString(10);

            return randomString + extension;
        }
        return generateRandomString(10);
    }

    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder stringBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = SECURE_RANDOM.nextInt(characters.length());
            stringBuilder.append(characters.charAt(randomIndex));
        }

        return stringBuilder.toString();
    }

}
