package com.gustas.videogamestore.service.image;

import com.gustas.videogamestore.domain.Image;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.zip.DataFormatException;

@Service
public interface ImageService {

    byte[] getImage(Long gameId) throws DataFormatException, IOException;
    Image saveImage(MultipartFile file) throws IOException;

}
