package com.gustas.videogamestore.service.rating;

import com.gustas.videogamestore.dto.request.RatingSubmitRequestDto;
import org.springframework.stereotype.Service;

@Service
public interface RatingService {

    void saveRating(RatingSubmitRequestDto ratingSubmitRequestDto);

}
