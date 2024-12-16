package com.gustas.videogamestore.controller;

import com.gustas.videogamestore.dto.request.CommentSubmitDto;
import com.gustas.videogamestore.dto.response.CommentsWithRepliesResponseDto;
import com.gustas.videogamestore.service.comment.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/comment")
@AllArgsConstructor
@Tag(name = "Comment Controllers")
public class CommentController {

    private CommentService commentService;

    @PostMapping("/save-comment/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    public void saveComment(@Valid @RequestBody CommentSubmitDto commentSubmitDto) {
        commentService.saveComment(commentSubmitDto);
    }

    @GetMapping("/get-comment/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentsWithRepliesResponseDto getComments(@PathVariable Long gameId) {
        return commentService.getCommentsWithReplies(gameId);
    }

    @PostMapping("/delete-comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }

}
