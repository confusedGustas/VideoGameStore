package com.gustas.videogamestore.mapper;

import com.gustas.videogamestore.dao.game.GameDao;
import com.gustas.videogamestore.domain.Rating;
import com.gustas.videogamestore.dto.request.RatingSubmitRequestDto;
import com.gustas.videogamestore.service.session.SessionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RatingMapper {

    private SessionService sessionService;
    private GameDao gameDao;

    public Rating toEntity(RatingSubmitRequestDto ratingSubmitRequestDto) {
        Rating rating = new Rating();

        rating.setValue(ratingSubmitRequestDto.getValue());
        rating.setGame(gameDao.getGame(ratingSubmitRequestDto.getGameId()));
        rating.setUser(sessionService.getUserFromSessionId());

        return rating;
    }

}
