package com.gustas.videogamestore.dao.rating;

import com.gustas.videogamestore.domain.Rating;

public interface RatingDao {

    void saveRating(Rating rating);
    Rating findByUserIdAndGameId(Long userId, Long gameId);
    Double findAverageRatingByGameId(Long gameId);

}
