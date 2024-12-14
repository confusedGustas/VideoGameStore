package com.gustas.videogamestore.service.image;

import com.gustas.videogamestore.dao.game.GameDao;
import com.gustas.videogamestore.dao.image.ImageDao;
import com.gustas.videogamestore.domain.Game;
import com.gustas.videogamestore.domain.Image;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Optional;
import java.util.zip.DataFormatException;

import static com.gustas.videogamestore.util.ImageUtil.getRandomFilenameWithExtension;

@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {

    private ImageDao imageDao;
    private GameDao gameDao;

    @Override
    public byte[] getImage(Long gameId) throws DataFormatException, IOException {
        Game game = gameDao.getGame(gameId);

        if (game == null) {
            throw new IllegalArgumentException("Game not found");
        }

        Optional<Image> image = imageDao.getImage(game.getImage().getId());

        if (image.isEmpty()) {
            throw new IllegalArgumentException("Image not found");
        }

        return image.get().getImageData();
    }

    @Override
    public Image saveImage(MultipartFile file) throws IOException {
        BufferedImage isImage = ImageIO.read(file.getInputStream());
        if (isImage == null) {
            throw new IllegalArgumentException("Invalid image");
        }

        Image image = new Image(file.getBytes(), getRandomFilenameWithExtension(file));
        imageDao.save(image);

        return image;
    }

}
