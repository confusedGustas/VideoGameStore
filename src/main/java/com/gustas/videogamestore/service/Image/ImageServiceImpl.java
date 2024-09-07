package com.gustas.videogamestore.service.Image;

import com.gustas.videogamestore.dao.Image.ImageDao;
import com.gustas.videogamestore.domain.Image;
import com.gustas.videogamestore.exception.ImageNotFoundException;
import com.gustas.videogamestore.util.ImageUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.zip.DataFormatException;

@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {

    private ImageDao imageDao;

    @Override
    public byte[] getImage(String name) throws DataFormatException, IOException {
        Optional<Image> image = imageDao.getImage(name);

        if (image.isEmpty()) {
            throw new ImageNotFoundException("Image " + name + " not found");
        }

        return ImageUtil.decompressImage(image.get().getImage());
    }

    @Override
    public void saveImage(MultipartFile file) throws IOException {
        BufferedImage isImage = ImageIO.read(file.getInputStream());
        if (isImage == null) {
            throw new IllegalArgumentException("Invalid image");
        }

        Image image = new Image(ImageUtil.compressImage(file.getBytes()), file.getOriginalFilename());

        imageDao.save(image);
    }

}
