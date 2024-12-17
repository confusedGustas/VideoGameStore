package com.gustas.videogamestore.dao.comment;

import com.gustas.videogamestore.domain.Comment;
import com.gustas.videogamestore.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentDaoImpl implements CommentDao {

    private CommentRepository commentRepository;

    @Override
    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> findByGameIdAndParentCommentIsNullOrderByTimeDesc(Long gameId) {
        return commentRepository.findByGameIdAndParentCommentIsNullOrderByTimeDesc(gameId);
    }

}
