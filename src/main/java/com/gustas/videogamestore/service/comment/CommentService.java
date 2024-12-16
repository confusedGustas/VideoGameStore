package com.gustas.videogamestore.service.comment;

import com.gustas.videogamestore.dto.request.CommentSubmitDto;
import com.gustas.videogamestore.dto.response.CommentsWithRepliesResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {

    void saveComment(CommentSubmitDto commentSubmitDto);
    CommentsWithRepliesResponseDto getCommentsWithReplies(Long gameId);
    void deleteComment(Long commentId);

}
