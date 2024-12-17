package com.gustas.videogamestore.dao.comment;

import com.gustas.videogamestore.domain.Comment;
import java.util.List;
import java.util.Optional;

public interface CommentDao {

    Optional<Comment> findById(Long id);
    Comment save(Comment comment);
    List<Comment> findByGameIdAndParentCommentIsNullOrderByTimeDesc(Long gameId);

}
