package com.gustas.videogamestore.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentSubmitDto {

    @NotNull(message = "Game ID is required")
    private Long gameId;

    @NotNull(message = "Content is required")
    private String content;

    @Nullable
    private Long parentCommentId;

}
