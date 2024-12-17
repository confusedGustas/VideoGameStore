package com.gustas.videogamestore.service.rating;

import com.gustas.videogamestore.dao.rating.RatingDao;
import com.gustas.videogamestore.domain.Rating;
import com.gustas.videogamestore.dto.request.RatingSubmitRequestDto;
import com.gustas.videogamestore.mapper.RatingMapper;
import com.gustas.videogamestore.service.session.SessionService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RatingServiceImpl implements RatingService {

    private RatingDao ratingDao;
    private RatingMapper ratingMapper;
    private SessionService sessionService;

    @Override
    @Transactional
    public void saveRating(RatingSubmitRequestDto ratingSubmitRequestDto) {
        Rating existingRating = ratingDao.findByUserIdAndGameId(sessionService.getIdFromSession(), ratingSubmitRequestDto.getGameId());

        if (existingRating != null) {
            existingRating.setValue(ratingSubmitRequestDto.getValue());
            ratingDao.saveRating(existingRating);
        } else {
            ratingDao.saveRating(ratingMapper.toEntity(ratingSubmitRequestDto));
        }
    }

}
