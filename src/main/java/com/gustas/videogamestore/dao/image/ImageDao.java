package com.gustas.videogamestore.dao.image;

import com.gustas.videogamestore.domain.Image;

import java.util.Optional;

public interface ImageDao {

    Optional<Image> getImage(String name);
    void save(Image image);

}
