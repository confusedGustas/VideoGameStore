package com.gustas.videogamestore.controller;

import com.gustas.videogamestore.service.image.ImageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("api/images")
@AllArgsConstructor
@Tag(name = "Image Controllers")
public class ImageController {

    private ImageService imageService;

    @GetMapping(value = "/get/{gameId}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> getImage(@PathVariable Long gameId) throws DataFormatException, IOException {
        return ResponseEntity.ok().body(imageService.getImage(gameId));
    }

}
