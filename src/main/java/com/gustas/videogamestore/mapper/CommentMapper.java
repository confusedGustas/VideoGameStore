package com.gustas.videogamestore.mapper;

import com.gustas.videogamestore.dao.comment.CommentDao;
import com.gustas.videogamestore.dao.game.GameDao;
import com.gustas.videogamestore.dao.user.UserDao;
import com.gustas.videogamestore.domain.Comment;
import com.gustas.videogamestore.dto.request.CommentSubmitDto;
import com.gustas.videogamestore.dto.response.CommentsResponseDto;
import com.gustas.videogamestore.service.session.SessionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CommentMapper {

    private GameDao gameDao;
    private UserDao userDao;
    private CommentDao commentDao;
    private SessionService sessionService;

    public Comment toEntity(CommentSubmitDto commentSubmitDto) {
        Comment comment = new Comment();

        comment.setGame(gameDao.getGame(commentSubmitDto.getGameId()));
        comment.setContent(commentSubmitDto.getContent());
        comment.setCreatedAt(LocalDateTime.now());

        if (commentSubmitDto.getParentCommentId() != null && commentSubmitDto.getParentCommentId() != 0) {
            comment.setParentComment(commentDao.findById(commentSubmitDto.getParentCommentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent comment not found")));
        } else comment.setParentComment(null);

        comment.setUser(userDao.findById(sessionService.getUserFromSessionId().getId()));

        return comment;
    }

    public CommentsResponseDto mapCommentToDtoWithReplies(Comment comment) {
        CommentsResponseDto dto = new CommentsResponseDto();

        dto.setCommentId(comment.getId());
        dto.setComment(comment.getContent());
        dto.setUsername(comment.getUser().getUsername());
        dto.setCreatedAt(comment.getCreatedAt().toString());
        dto.setParentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null);

        List<CommentsResponseDto> replies = comment.getReplies().stream()
                .map(this::mapCommentToDtoWithReplies)
                .collect(Collectors.toList());

        dto.setReplies(replies);

        return dto;
    }

}
