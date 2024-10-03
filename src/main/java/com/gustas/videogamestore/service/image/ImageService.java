package com.gustas.videogamestore.service.image;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.zip.DataFormatException;

@Service
public interface ImageService {

    byte[] getImage(String name) throws DataFormatException, IOException;
    void saveImage(MultipartFile file) throws IOException;

}
