package com.gustas.videogamestore.dao.rating;

import com.gustas.videogamestore.domain.Rating;
import com.gustas.videogamestore.repository.RatingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RatingDaoImpl implements RatingDao {

    private RatingRepository ratingRepository;

    @Override
    public void saveRating(Rating rating) {
        ratingRepository.save(rating);
    }

    @Override
    public Rating findByUserIdAndGameId(Long userId, Long gameId) {
        return ratingRepository.findByUserIdAndGameId(userId, gameId);
    }

    @Override
    public Double findAverageRatingByGameId(Long gameId) {
        return ratingRepository.findAverageRatingByGameId(gameId);
    }

}
