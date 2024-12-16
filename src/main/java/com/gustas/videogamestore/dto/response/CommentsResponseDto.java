package com.gustas.videogamestore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentsResponseDto {

    private Long commentId;
    private String comment;
    private String username;
    private String createdAt;
    private Long parentCommentId;
    private List<CommentsResponseDto> replies;

}
