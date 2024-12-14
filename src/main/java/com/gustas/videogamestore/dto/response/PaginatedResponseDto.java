package com.gustas.videogamestore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedResponseDto {

    private List<GameResponseDto> items;
    private Integer currentPage;
    private Integer totalPages;
    private Long totalItems;

}
