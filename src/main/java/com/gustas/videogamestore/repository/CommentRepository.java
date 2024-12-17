package com.gustas.videogamestore.repository;

import com.gustas.videogamestore.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.game.id = :gameId AND c.parentComment IS NULL ORDER BY c.createdAt DESC")
    List<Comment> findByGameIdAndParentCommentIsNullOrderByTimeDesc(@Param("gameId") Long gameId);

}