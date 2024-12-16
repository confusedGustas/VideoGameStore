package com.gustas.videogamestore.repository;

import com.gustas.videogamestore.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    Rating findByUserIdAndGameId(Long userId, Long gameId);
    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.game.id = :gameId")
    Double findAverageRatingByGameId(@Param("gameId") Long gameId);

    @Query("SELECT r.rating FROM Rating r WHERE r.user.id = :userId AND r.game.id = :gameId")
    Integer findCurrentUserRating(@Param("userId") Long userId, @Param("gameId") Long gameId);

}
