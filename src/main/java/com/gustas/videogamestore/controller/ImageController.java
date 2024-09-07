package com.gustas.videogamestore.controller;

import com.gustas.videogamestore.service.Image.ImageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("api/images")
@AllArgsConstructor
@Tag(name = "Image Controllers")
public class ImageController {

    private ImageService imageService;

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void saveImage(@RequestParam("image") MultipartFile file) throws IOException {
        imageService.saveImage(file);
    }

    @GetMapping(value = "/get/{name}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<byte[]> getImage(@PathVariable String name) throws DataFormatException, IOException {
        return ResponseEntity.ok().body(imageService.getImage(name));
    }

}
