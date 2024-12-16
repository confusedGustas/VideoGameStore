package com.gustas.videogamestore.controller;

import com.gustas.videogamestore.dto.request.RatingSubmitRequestDto;
import com.gustas.videogamestore.service.rating.RatingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/ratings")
@AllArgsConstructor
@Tag(name = "Rating Controllers")
public class RatingController {

    private RatingService ratingService;

    @PostMapping("/add-rating")
    @ResponseStatus(HttpStatus.OK)
    public void logoutUser(@Valid @RequestBody RatingSubmitRequestDto ratingSubmitRequestDto) {
        ratingService.saveRating(ratingSubmitRequestDto);
    }

}
