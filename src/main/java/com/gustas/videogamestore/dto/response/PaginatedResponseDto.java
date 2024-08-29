package com.gustas.videogamestore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class PaginatedResponseDto {

    private List<GameResponseDto> items;
    private int currentPage;
    private int totalPages;
    private long totalItems;

}
