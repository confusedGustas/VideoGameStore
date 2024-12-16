package com.gustas.videogamestore.service.comment;

import com.gustas.videogamestore.dao.comment.CommentDao;
import com.gustas.videogamestore.domain.Comment;
import com.gustas.videogamestore.dto.request.CommentSubmitDto;
import com.gustas.videogamestore.dto.response.CommentsResponseDto;
import com.gustas.videogamestore.dto.response.CommentsWithRepliesResponseDto;
import com.gustas.videogamestore.mapper.CommentMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private CommentDao commentDao;
    private CommentMapper commentMapper;

    @Override
    public void saveComment(CommentSubmitDto commentSubmitDto) {
        commentDao.save(commentMapper.toEntity(commentSubmitDto));
    }

    @Override
    @Transactional
    public CommentsWithRepliesResponseDto getCommentsWithReplies(Long gameId) {
        List<Comment> rootComments = commentDao.findByGameIdAndParentCommentIsNull(gameId);
        List<CommentsResponseDto> commentDto = rootComments.stream()
                .map(commentMapper::mapCommentToDtoWithReplies)
                .collect(Collectors.toList());
        return new CommentsWithRepliesResponseDto(gameId, commentDto);
    }

    @Override
    public void deleteComment(Long commentId) {
        Comment comment = commentDao.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        comment.setContent("Comment deleted!");
        commentDao.save(comment);
    }

}
